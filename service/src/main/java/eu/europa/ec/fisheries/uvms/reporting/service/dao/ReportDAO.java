/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import static eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter.with;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.*;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Report.EXECUTED_BY_USER;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ActivityReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

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
            throw new ReportingServiceException("SoftDelete failed", re);
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
            throw new ReportingServiceException("Visibility change failed", re);
        }
        log.debug("[END] changeVisibility(...)");
    }


    public Report findReportByReportId(final Long id, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException {
        Report result = null;
        List<Report> reports;
        try {
            getSession().enableFilter(EXECUTED_BY_USER).setParameter("username", username);
            reports = findEntityByNamedQuery(Report.class, Report.FIND_BY_ID, with("reportID", id).and("scopeName", scopeName).and("username", username).and("isAdmin", isAdmin?1:0).parameters(), 1);
        } catch (Exception exc) {
            throw new ReportingServiceException("FindReport failed", exc);
        }
        if (reports != null && !reports.isEmpty()) {
            result = reports.get(0);
        }
        return result;
    }

    public List<ActivityReportDTO> findActivityReportByReportId(final Report report, int firstResult, int maxResults) {

//        long offset = pageNumber != null ? pageNumber * pageSize : 0;
//        long limit = pageSize != null ? pageSize : 1000;

        StringBuilder query =
                new StringBuilder()
                        .append("SELECT DISTINCT a.id,a.act_id,a.fa_report_id,a.trip_id,a.report_type,a.activity_type,a.purpose_code,a.source,a.accepted_timestamp,a.calculated_timestamp," +
                                " a.occurrence_timestamp,a.start_timestamp,a.end_timestamp,a.status,a.is_latest,a.activity_coordinates,a.activity_latitude,a.activity_longitude,a.master,a.reason_code,a.asset_hist_guid AS activity_assetHistGuid," +
                                "ra.asset_hist_guid,ra.asset_guid,ra.asset_hist_active,ra.uvi,ra.iccat,ra.cfr,ra.ircs,ra.name,ra.ext_mark,ra.gfcm,ra.country_code,ra.length_overall,ra.main_gear_type," +
                                "t.id AS trip_trip_id,t.trip_id AS trip_tripid,t.trip_id_scheme,t.trip_coordinates,t.first_fishing_activity,t.first_fishing_activity_timestamp,t.last_fishing_activity,t.last_fishing_activity_timestamp,t.trip_duration," +
                                "t.number_of_corrections,t.number_of_positions,t.asset_hist_guid AS trip_assetHistGuid ")
                        .append("FROM reporting.activity a ")
                        .append("LEFT JOIN reporting.asset ra ON ra.asset_hist_guid = a.asset_hist_guid ")
                        .append("LEFT JOIN reporting.trip t ON t.trip_id = a.trip_id ")
                        .append("LEFT JOIN reporting.activity_catch ac ON ac.activity_id = a.act_id ")
                        .append("LEFT JOIN reporting.activity_catch_location acl ON acl.activity_catch_id = ac.id ")
                        .append("LEFT JOIN reporting.activity_catch_processing acp ON acp.activity_catch_id = ac.id ")
                        .append("LEFT JOIN reporting.activity_gear ag ON ag.activity_id = a.act_id ")
                        .append("LEFT JOIN reporting.activity_location al ON al.activity_id = a.act_id ");

            if(!report.getFilters().isEmpty()){
                query.append("WHERE ");

                report.getFilters().stream().forEach(f -> {
                    FaFilter faFilter;
                    AssetFilter assetFilter;
                    switch (f.getType()) {
                        case fa:
                            faFilter = (FaFilter) f;
                            if(faFilter.getReportTypes() != null && !faFilter.getReportTypes().isEmpty()) {
                                query.append(" a.report_type = '");
                                query.append(faFilter.getReportTypes().get(0) + "'"+ " AND ");
                            }

                            if(faFilter.getFaPorts() != null && !faFilter.getFaPorts().isEmpty()) {
                                query.append(" al.code = '");
                                query.append(faFilter.getFaPorts().get(0) + "'"+ " AND ");
                            }

                            if(faFilter.getMasters() != null && !faFilter.getMasters().isEmpty()) {
                                query.append(" a.master like '%");
                                query.append(faFilter.getMasters().get(0) + "%'" + " AND ");
                            }

                            if(faFilter.getSpecies() != null && !faFilter.getSpecies().isEmpty()){
                                query.append(" ac.species_code in ('");
                                query.append( faFilter.getSpecies().get(0) + "')" +" AND ");
                            }

                            if(faFilter.getFaWeight() != null && faFilter.getFaWeight().getWeightMax() != null){
                                query.append(" ac.weight_measure <= " + faFilter.getFaWeight().getWeightMax()+ " AND ");
                            }

                            if(faFilter.getFaWeight() != null && faFilter.getFaWeight().getWeightMin() != null){
                                query.append(" ac.weight_measure >= " + faFilter.getFaWeight().getWeightMin() +" AND ");
                            }

                            if(faFilter.getFaGears() != null && !faFilter.getFaGears().isEmpty()){
                                query.append(" ag.gear_code = '"+ faFilter.getFaGears().get(0) +"'" + " AND ");
                            }

                            if(faFilter.getActivityTypes() != null && !faFilter.getActivityTypes().isEmpty()){
                                query.append(" a.activity_type = '"+ faFilter.getActivityTypes().get(0) +"'" + " AND ");
                            }

                            if(faFilter.getActivityTypes() != null && !faFilter.getActivityTypes().isEmpty()){
                                query.append(" a.activity_type = '"+ faFilter.getActivityTypes().get(0) +"'" + " AND ");
                            }

                            break;

                        case asset:
                            assetFilter = (AssetFilter) f;
                            if(assetFilter.getName() != null ) {
                                query.append(" ra.guid = '");
                                query.append(assetFilter.getGuid() + "'"+ " AND ");
                            }
                            break;
                        default:
                    }
                });
            }


//        query.append("ORDER BY m.id DESC ");
//        query.append("OFFSET " + offset + " LIMIT " + limit);
        Query nativeQuery = em.createNativeQuery(query.toString().substring(0,query.toString().length() - 4),Activity.class);
//        nativeQuery.setFirstResult(firstResult);
//        nativeQuery.setMaxResults(maxResults);


        return (List<ActivityReportDTO>) nativeQuery.getResultList();
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
            throw new ReportingServiceException("list failed", exc);
        }
    }

    public List<Report> listTopExecutedReportByUsernameAndScope(String username, String scopeName, Boolean existent, boolean isAdmin, Integer numberOfReport) throws ReportingServiceException {

        try {
            List<Report> listReports = new ArrayList<>(findEntityByNamedQuery(Report.class, Report.LIST_TOP_EXECUTED_BY_DATE,
                    with("scopeName", scopeName).and("username", username).and("existent", existent).parameters(), numberOfReport));
            if (listReports.isEmpty() || (listReports.size() < numberOfReport)) {
                listReports.addAll(findEntityByNamedQuery(Report.class, Report.LIST_BY_CREATION_DATE,
                        with("scopeName", scopeName).and("username", username).and("existent", existent).and("isAdmin", isAdmin?1:0).parameters(), numberOfReport - listReports.size()));
            }
            return listReports;
        } catch (Exception e) {
            throw new ReportingServiceException(e.getMessage(), e);
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