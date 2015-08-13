package eu.europa.ec.fisheries.uvms.reporting.service.dao;

// Generated Aug 6, 2015 11:44:30 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.ModuleInitializerBean;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLogEntity;

/**
 * Home object for domain model class ReportExecutionLogEntity.
 * @see eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLogEntity
 * @author Hibernate Tools
 */
@Stateless
public class ReportExecutionLogDAO {

	private static final Logger log = LoggerFactory.getLogger(ReportExecutionLogDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ReportExecutionLogEntity transientInstance) {
		log.debug("persisting ReportExecutionLogEntity instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ReportExecutionLogEntity persistentInstance) {
		log.debug("removing ReportExecutionLogEntity instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ReportExecutionLogEntity merge(ReportExecutionLogEntity detachedInstance) {
		log.debug("merging ReportExecutionLogEntity instance");
		try {
			ReportExecutionLogEntity result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ReportExecutionLogEntity findById(long id) {
		log.debug("getting ReportExecutionLogEntity instance with id: " + id);
		try {
			ReportExecutionLogEntity instance = entityManager.find(
					ReportExecutionLogEntity.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
