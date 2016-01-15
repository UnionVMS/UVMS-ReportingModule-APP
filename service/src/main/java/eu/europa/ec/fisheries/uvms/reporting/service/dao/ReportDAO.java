package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Report.EXECUTED_BY_USER;
import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;

@Slf4j
public class ReportDAO extends AbstractDAO<Report> {

    private EntityManager em;

    public ReportDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * does logical/soft delete
     *
     * @param report
     */
    protected void softDelete(Report report, String username) throws ReportingServiceException {

        log.debug("{} is removing ReportEntity instance", username);
        try {
            report.setDeletedBy(username);
            report.setDeletedOn(DateUtils.nowUTC().toDate());
            report.setIsDeleted(true);
            Session session = em.unwrap(Session.class);
            session.update(report);
            session.flush();
            log.debug("softDelete successful");
        } catch (RuntimeException re) {
            String errorMessage = "softDelete failed";

            log.error(errorMessage, re);
            throw new ReportingServiceException(errorMessage, re);
        }
    }

    /**
     * does logical/soft delete
     *
     * @param entityId
     */
    public void softDelete(Long entityId, String username, String scopeName) throws ReportingServiceException {
        Report persistentInstance = this.findReportByReportId(entityId, username, scopeName);
        if (persistentInstance == null) {
            throw new ReportingServiceException("Non existing report entity cannot be deleted.");
        }

        softDelete(persistentInstance, username);
    }

    public Report findReportByReportId(final Long id, String username, String scopeName) throws ReportingServiceException {
        Report result = null;
        List<Report> reports;
        try {
            getSession().enableFilter(EXECUTED_BY_USER).setParameter("username", username);
            reports = findEntityByNamedQuery(Report.class, Report.FIND_BY_ID, with("reportID", id).and("scopeName", scopeName).and("username", username).parameters(), 1);
        } catch (ServiceException exc) {
            log.error("findReport failed", exc);
            throw new ReportingServiceException("findReport failed", exc);
        }
        if (reports != null && !reports.isEmpty()) {
            result = reports.get(0);
        }
        return result;
    }

    public List<Report> listByUsernameAndScope(String username, String scopeName, Boolean existent) throws ReportingServiceException {
        log.debug("Searching for ReportEntity instances with username: {} and scopeName:{}", username, scopeName);

        try {
            getSession().enableFilter(EXECUTED_BY_USER).setParameter("username", username);
            List<Report> listReports =
                    findEntityByNamedQuery(Report.class, Report.LIST_BY_USERNAME_AND_SCOPE,
                            with("scopeName", scopeName).and("username", username).and("existent", existent).parameters());
            log.debug("list successful");


            return listReports;
        } catch (Exception exc) {
            log.error("list failed", exc);
            throw new ReportingServiceException("list failed", exc);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    private Session getSession() {
        return em.unwrap(Session.class);
    }

}