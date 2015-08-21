package eu.europa.ec.fisheries.uvms.reporting.service.dao;

// Generated Aug 6, 2015 11:44:30 AM by Hibernate Tools 4.3.1

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;



import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.impl.QueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.model.ReportExecutionLog;
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
		String username = context.getCallerPrincipal().getName();
		
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
	public void remove(long entityId) {
			ReportEntity persistentInstance = this.findById(entityId);
			
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
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("scopeId", scopeId);
		
		try {
			Query query = session.createQuery("from ReportEntity r where (r.createdBy=:username and r.scopeId = :scopeId) or (r.isShared = 'Y' and r.scopeId = :scopeId))");
			query = query.setParameter("username", username).setParameter("scopeId", scopeId);
			
			Collection<ReportEntity> listReports = query.list();
		
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
