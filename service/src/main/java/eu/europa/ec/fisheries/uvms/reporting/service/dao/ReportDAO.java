package eu.europa.ec.fisheries.uvms.reporting.service.dao;

// Generated Aug 6, 2015 11:44:30 AM by Hibernate Tools 4.3.1

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;



import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.impl.QueryImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.model.ReportExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ModuleInitializerBean;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportBean;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLogEntity;

/**
 * Home object for domain model class ReportEntity.
 * @see eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity
 * @author Hibernate Tools
 */
@Stateless
@LocalBean
public class ReportDAO {

	private static final Logger log = LoggerFactory.getLogger(ReportDAO.class);

	@PersistenceContext
	private Session session;
	
	@Resource
	private SessionContext context;

	@Transactional
	public ReportEntity persist(ReportEntity transientInstance) {
		log.debug("persisting ReportEntity instance");
		try {
			session.saveOrUpdate(transientInstance);
			session.flush();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
		
		return transientInstance;
	}

	@Transactional
	public void persist(ReportExecutionLogEntity transientInstance) {
		log.debug("persisting ReportExecutionLogEntity instance");
		try {
			session.saveOrUpdate(transientInstance);
			session.flush();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	/**
	 * does logical/soft delete
	 * @param persistentInstance
	 */
	@Transactional
	public void remove(ReportEntity persistentInstance) {
		//String username = context.getCallerPrincipal().getName();
		String username = "georgi"; //TODO remove the hardcoded username and use the caller principal instead
		
		log.debug(username + " is removing ReportEntity instance");
		try {
			persistentInstance.setDeletedBy(username);
			persistentInstance.setDeletedOn(new Date());
			persistentInstance.setIsDeleted(true);
			this.persist(persistentInstance);
			session.flush();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}
	
	/**
	 * does logical/soft delete
	 * @param entityId
	 */
	@Transactional
	public void remove(long entityId) throws ReportingServiceException{
			ReportEntity persistentInstance = this.findById(entityId);
			if (persistentInstance == null) {
				throw new ReportingServiceException("Non existing report entity cannot be deleted.");
			}
			
			this.remove(persistentInstance);
	}

	public ReportEntity findById(long id) {
		log.debug("getting ReportEntity instance with id: " + id);
		try {
			ReportEntity instance = (ReportEntity) session.createQuery("from ReportEntity r where r.id = :reportID").setParameter("reportID", id).uniqueResult();
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Collection<ReportEntity> findByUsernameAndScope(String username, long scopeId) {
		log.debug("Searching for ReportEntity instances with username: " + username + " and scopeID:" + scopeId);
		
	
		try {
			Query query = session.createSQLQuery("select * from reporting.report as r "
					+ "left outer join (select report_id, executed_by, max(executed_on) as executed_on from reporting.report_execution_log where executed_by = :username group by executed_by, report_id) as l "
					+ "on r.id = l.report_id "
					+ "where (r.is_deleted <> 'Y') and ((r.scope_id = :scopeId and (r.created_by = :username or r.visibility = 'SCOPE') ) or r.visibility = 'GLOBAL') "
					+ "order by r.id;"); 
			query = query.setParameter("username", username).setParameter("scopeId", scopeId);
			
			List<Object[]> resultSet = query.list();
			Collection<ReportEntity> listReports = new ArrayList<ReportEntity>(resultSet.size());
			
			for (Object[] result : resultSet) {
				ReportEntity entity = new ReportEntity();
				ReportExecutionLogEntity logEntity = new ReportExecutionLogEntity();
				Set<ReportExecutionLogEntity> logSet = new HashSet<ReportExecutionLogEntity>(); 
				
				entity.setId(((BigInteger) result[0]).longValue());
				entity.setName((String) result[1]);
				entity.setDescription(result[2]!= null?(String)result[2]:null);
				entity.setFilterExpression((String) result[3]);
				entity.setOutComponents(result[4]!= null?(String)result[4]:null);
				entity.setScopeId(result[5]!= null?((BigInteger) result[5]).longValue():null);
				entity.setCreatedBy((String) result[6]);
				entity.setCreatedOn((Date) result[7]);
				entity.setVisibility(VisibilityEnum.valueOf((String) result[11]));
				
				if (result[12] != null) { //then we have a execution log
					logEntity.setExecutedBy((String) result[13]);
					logEntity.setExecutedOn((Date) result[14]);
					logSet.add(logEntity);
				}
				
				entity.setReportExecutionLogs(logSet);
				
				listReports.add(entity);
			}
		
			log.debug("get successful");
			return listReports;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 * deletes physically the record from the DB
	 * @param persistentInstance
	 */
	@Transactional
	public void delete(ReportEntity persistentInstance) {
		log.debug("deleting ReportEntity instance with id: " + persistentInstance.getId());
		try {
			session.delete(session.merge(persistentInstance));
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	
/*	public void addReportExecutionLog(ReportExecutionLog logEntry) {
		log.debug("Adding ReportExecutionLog" );
		
		try {
			//TODO add logEntry 
			session.
			log.debug("addReportExecutionLog  successful");
		} catch (RuntimeException re) {
			log.error("addReportExecutionLog failed", re);
			throw re;
		}
	}*/
}
