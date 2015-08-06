package eu.europa.ec.fisheries.uvms.reporting.service.dao;

// Generated Aug 6, 2015 11:44:30 AM by Hibernate Tools 4.3.1

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

/**
 * Home object for domain model class Report.
 * @see eu.europa.ec.fisheries.uvms.reporting.service.entities.Report
 * @author Hibernate Tools
 */
@Stateless
@Local(ReportLocal.class)
public class ReportHome {

	private static final Log log = LogFactory.getLog(ReportHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Report transientInstance) {
		log.debug("persisting Report instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Report persistentInstance) {
		log.debug("removing Report instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Report merge(Report detachedInstance) {
		log.debug("merging Report instance");
		try {
			Report result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Report findById(long id) {
		log.debug("getting Report instance with id: " + id);
		try {
			Report instance = entityManager.find(Report.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
