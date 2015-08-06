package eu.europa.ec.fisheries.uvms.reporting.service.dao;

// Generated Aug 6, 2015 11:44:30 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLog;

/**
 * Home object for domain model class ReportExecutionLog.
 * @see eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLog
 * @author Hibernate Tools
 */
@Stateless
public class ReportExecutionLogHome {

	private static final Log log = LogFactory
			.getLog(ReportExecutionLogHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ReportExecutionLog transientInstance) {
		log.debug("persisting ReportExecutionLog instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ReportExecutionLog persistentInstance) {
		log.debug("removing ReportExecutionLog instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ReportExecutionLog merge(ReportExecutionLog detachedInstance) {
		log.debug("merging ReportExecutionLog instance");
		try {
			ReportExecutionLog result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ReportExecutionLog findById(long id) {
		log.debug("getting ReportExecutionLog instance with id: " + id);
		try {
			ReportExecutionLog instance = entityManager.find(
					ReportExecutionLog.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
