package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.DAO;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;

public class ReportDAO extends AbstractDAO<Report> implements DAO<Report> {

    private static final Logger log = LoggerFactory.getLogger(ReportDAO.class);

    private EntityManager em;

    public ReportDAO(EntityManager em){
        this.em = em;
    }

    public Report saveOrUpdate(Report report) throws ServiceException {
        //check whether it's an update or create
        if (report.getId() > 0) {
            log.debug("update ReportEntity instance");
            Report oldEntity = findReportByReportId(report.getId());

            if (oldEntity != null) {
                oldEntity.setDescription(report.getDescription());
                oldEntity.setFilters(report.getFilters());
                oldEntity.setName(report.getName());
                oldEntity.setOutComponents(report.getOutComponents());
                oldEntity.setVisibility(report.getVisibility());

                try {
                    Session session = em.unwrap(Session.class);

                    session.update(oldEntity);
                    session.flush();
                    log.debug("update successful");
                } catch (RuntimeException re) {
                    log.error("update failed", re);
                    throw re;
                }
            } else {
                log.error("updating non-existing report entity with ID=" + report.getId());
                throw new RuntimeException("updating non-existing report entity");
            }
        } else {
            log.debug("persisting ReportEntity instance");
            try {
                Session session = em.unwrap(Session.class);

                session.save(report);
                session.flush();
                log.debug("update successful");
            } catch (RuntimeException re) {
                log.error("update failed", re);
                throw re;
            }
        }

        return report;
    }

    public void persist(ExecutionLog transientInstance) throws ServiceException {
        log.debug("persisting ReportExecutionLogEntity instance");
        try {
            Session session = em.unwrap(Session.class);

            session.saveOrUpdate(transientInstance);
            session.flush();
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }

    /**
     * does logical/soft delete
     * @param report
     */
    public void softDelete(Report report) throws ServiceException {
        //String username = context.getCallerPrincipal().getName();
        String username = "georgi"; //TODO softDelete the hardcoded username and use the caller principal instead

        log.debug(username + " is removing ReportEntity instance");
        try {
            report.setDeletedBy(username);
            report.setDeletedOn(new Date());
            report.setIsDeleted(true);
            Session session = em.unwrap(Session.class);
            session.update(report);
            session.flush();
            log.debug("softDelete successful");
        } catch (RuntimeException re) {
            log.error("softDelete failed", re);
            throw re;
        }
    }

    /**
     * does logical/soft delete
     * @param entityId
     */
    public void softDelete(long entityId) throws ServiceException{
        Report persistentInstance = this.findReportByReportId(entityId);
        if (persistentInstance == null) {
            throw new ServiceException("Non existing report entity cannot be deleted.");
        }

        softDelete(persistentInstance);
    }

    @Transactional
    public Report findReportByReportId(final Long id) throws ServiceException {
        Report result = null;
        List<Report> reports = findEntityByNamedQuery(Report.class, Report.FIND_BY_ID, with("reportID", id).parameters(), 1);
        if (reports != null && reports.size() > 0){
            result = reports.get(0);
        }
        return result;
    }

    public List<Report> listByUsernameAndScope(String username, long scopeId) throws ServiceException {
        log.debug("Searching for ReportEntity instances with username: " + username + " and scopeID:" + scopeId);

        try {
            List<Report> listReports =
                    findEntityByNamedQuery(Report.class, Report.LIST_BY_USERNAME_AND_SCOPE,
                            with("scopeId", scopeId).and("username", username).parameters());
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
    public void delete(Report persistentInstance) throws ServiceException {
        log.debug("deleting ReportEntity instance with id: " + persistentInstance.getId());
        try {
            Session session = em.unwrap(Session.class);
            session.delete(session.merge(persistentInstance));
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}