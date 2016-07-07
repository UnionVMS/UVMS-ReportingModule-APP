/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries � European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
     * @param isAdmin
     */
    public void softDelete(Long entityId, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException {
        Report persistentInstance = this.findReportByReportId(entityId, username, scopeName, isAdmin);
        if (persistentInstance == null) {
            throw new ReportingServiceException("Non existing report entity cannot be deleted.");
        }

        softDelete(persistentInstance, username);
    }

    /**
     * changes report visibility
     *
     * @param entityId
     * @param newVisibility
     * @param username
     * @param scopeName
     * @throws ReportingServiceException
     */
    public void changeVisibility(Long entityId, VisibilityEnum newVisibility, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException {
        log.debug("[START] changeVisibility({},{},{},{})", entityId, newVisibility, username, scopeName);
        Report persistentInstance = this.findReportByReportId(entityId, username, scopeName, isAdmin);
        if (persistentInstance == null) {
            throw new ReportingServiceException("Non existing report entity cannot be deleted.");
        }

        try {
            persistentInstance.setVisibility(newVisibility);
            Session session = em.unwrap(Session.class);
            session.update(persistentInstance);
            session.flush();
            log.debug("visibility successfully changed.");
        } catch (RuntimeException re) {
            String errorMessage = "visibility change failed";

            log.error(errorMessage, re);
            throw new ReportingServiceException(errorMessage, re);
        }
        log.debug("[END] changeVisibility(...)");
    }


    public Report findReportByReportId(final Long id, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException {
        Report result = null;
        List<Report> reports;
        try {
            getSession().enableFilter(EXECUTED_BY_USER).setParameter("username", username);
            reports = findEntityByNamedQuery(Report.class, Report.FIND_BY_ID, with("reportID", id).and("scopeName", scopeName).and("username", username).and("isAdmin", isAdmin?1:0).parameters(), 1);
        } catch (ServiceException exc) {
            log.error("findReport failed", exc);
            throw new ReportingServiceException("findReport failed", exc);
        }
        if (reports != null && !reports.isEmpty()) {
            result = reports.get(0);
        }
        return result;
    }

    public List<Report> listByUsernameAndScope(String username, String scopeName, Boolean existent, Boolean isAdmin) throws ReportingServiceException {
        log.debug("Searching for ReportEntity instances with username: {} and scopeName:{}", username, scopeName);

        try {
            getSession().enableFilter(EXECUTED_BY_USER).setParameter("username", username);
            List<Report> listReports =
                    findEntityByNamedQuery(Report.class, Report.LIST_BY_USERNAME_AND_SCOPE,
                            with("scopeName", scopeName).and("username", username).and("existent", existent).and("isAdmin", isAdmin?1:0).parameters());
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

    public List<Report> listTopExecutedReportByUsernameAndScope(String username, String scopeName, Boolean existent, boolean isAdmin, Integer numberOfReport) throws ReportingServiceException {

        try {
            List<Report> listReports = new ArrayList<>();
            listReports.addAll(findEntityByNamedQuery(Report.class, Report.LIST_TOP_EXECUTED_BY_DATE,
                    with("scopeName", scopeName).and("username", username).and("existent", existent).and("isAdmin", isAdmin ? 1 : 0).parameters(), numberOfReport));
            if (listReports == null || listReports.isEmpty() || (listReports.size() < numberOfReport)) {
                listReports.addAll(findEntityByNamedQuery(Report.class, Report.LIST_BY_CREATION_DATE,
                        with("scopeName", scopeName).and("username", username).and("existent", existent).and("isAdmin", isAdmin?1:0).parameters(), numberOfReport - listReports.size()));
            }

            return listReports;
        } catch (ServiceException e) {
            throw new ReportingServiceException(e.getMessage());
        }
    }
}