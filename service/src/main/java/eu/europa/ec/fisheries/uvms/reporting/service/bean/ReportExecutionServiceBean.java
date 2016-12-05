/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.*;
import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapperV2;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.reporting.model.Constants.*;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Stateless
@Local(value = ReportExecutionService.class)
@Slf4j
@Interceptors(TracingInterceptor.class)
public class ReportExecutionServiceBean implements ReportExecutionService {

    private @EJB ReportRepository repository;
    private @EJB AuditService auditService;
    private @EJB AssetServiceBean assetModule;
    private @EJB MovementServiceBean movementModule;
    private @EJB SpatialService spatialModule;
    private @EJB ActivityService activityService;

    @Override
    @Transactional
    @IAuditInterceptor(auditActionType = AuditActionEnum.EXECUTE)
    public ExecutionResultDTO getReportExecutionByReportId(final Long id, final String username, final String scopeName, final List<AreaIdentifierType> areaRestrictions, final DateTime now, Boolean isAdmin) throws ReportingServiceException {
        log.debug("[START] getVmsDataByReportId({}, {}, {})", username, scopeName, id);
        Report reportByReportId = repository.findReportByReportId(id, username, scopeName, isAdmin);
        if (reportByReportId == null) {
            final String error = "No report found with id " + id;
            log.error(error);
            throw new ReportingServiceException(error);
        }
        ExecutionResultDTO resultDTO = executeReport(reportByReportId, now, areaRestrictions);
        reportByReportId.updateExecutionLog(username);
        log.debug("[END] getReportExecutionByReportId(...)");
        return resultDTO;
    }

    @Override
    public ExecutionResultDTO getReportExecutionWithoutSave(final eu.europa.ec.fisheries.uvms.reporting.model.vms.Report report, final List<AreaIdentifierType> areaRestrictions, String userName) throws ReportingServiceException {
        Map additionalProperties = (Map) report.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);
        DateTime dateTime = DateUtils.UI_FORMATTER.parseDateTime((String) additionalProperties.get(TIMESTAMP));
        Report toReport = ReportMapperV2.INSTANCE.reportDtoToReport(report);

        ExecutionResultDTO resultDTO = executeReport(toReport, dateTime, areaRestrictions);
        auditService.sendAuditReport(AuditActionEnum.EXECUTE, report.getName(), userName);
        return resultDTO;
    }

    private ExecutionResultDTO executeReport(Report report, DateTime dateTime, List<AreaIdentifierType> areaRestrictions) throws ReportingServiceException {
        try {
            FilterProcessor processor = new FilterProcessor(report.getFilters(), dateTime);
            String wkt = getFilterAreaWkt(processor, areaRestrictions);
            ExecutionResultDTO resultDTO = new ExecutionResultDTO();
            getVmsData(resultDTO, processor, wkt, dateTime);
            getFishingTripsAndActivities(resultDTO, processor, wkt, report.getReportType());
            return resultDTO;
        } catch (ProcessorException e) {
            String error = "Error while processing reporting filters";
            log.error(error, e);
            throw new ReportingServiceException(error, e);
        }
    }

    private void getFishingTripsAndActivities(ExecutionResultDTO resultDTO, FilterProcessor processor, String wkt, ReportTypeEnum reportType) throws ReportingServiceException {
        List<SingleValueTypeFilter> singleValueTypeFilters = getSingleValueFilters(processor, wkt);
        List<ListValueTypeFilter> listValueTypeFilters = getListValueFilters(processor, reportType, resultDTO.getAssetMap());
        FishingTripResponse tripResponse = activityService.getFishingTrips(singleValueTypeFilters, listValueTypeFilters);
        resultDTO.setTrips(tripResponse.getFishingTripIdLists());
        resultDTO.setActivities(tripResponse.getFishingActivityLists());
    }

    private void getVmsData(ExecutionResultDTO resultDTO, FilterProcessor processor, String wkt, DateTime dateTime) throws ReportingServiceException {

        try {
            Collection<MovementMapResponseType> movementMap;
            Map<String, MovementMapResponseType> responseTypeMap;
            Map<String, Asset> assetMap;

            processor.addAreaCriteria(wkt);
            log.debug("Running report {} assets or asset groups.", processor.hasAssetsOrAssetGroups() ? "has" : "doesn't have");

            if (processor.hasAssetsOrAssetGroups()) {
                assetMap = assetModule.getAssetMap(processor);
                processor.getMovementListCriteria().addAll(ExtMovementMessageMapper.movementListCriteria(assetMap.keySet()));
                movementMap = movementModule.getMovement(processor);
            } else {
                responseTypeMap = movementModule.getMovementMap(processor);
                Set<String> assetGuids = responseTypeMap.keySet();
                movementMap = responseTypeMap.values();
                processor.getAssetListCriteriaPairs().addAll(ExtAssetMessageMapper.assetCriteria(assetGuids));
                assetMap = assetModule.getAssetMap(processor);
            }

            resultDTO.setAssetMap(assetMap);
            resultDTO.setMovementMap(movementMap);

        } catch (ReportingServiceException e) {
            String error = "Exception during retrieving filter area";
            log.error(error, e);
            throw new ReportingServiceException(error, e);
        }
    }

    private List<SingleValueTypeFilter> getSingleValueFilters(FilterProcessor processor, String areaWkt) {
        List<SingleValueTypeFilter> filterTypes = new ArrayList<>();
        filterTypes.addAll(processor.getSingleValueTypeFilters()); // Add all the Fa filter criteria from the filters

        log.info("generate filtered area WKT by combining area filters and scope area");
        filterTypes.add(new SingleValueTypeFilter(SearchFilter.AREA_GEOM, areaWkt));
        return filterTypes;
    }

    private List<ListValueTypeFilter> getListValueFilters(FilterProcessor processor, ReportTypeEnum reportType, Map<String, Asset> assetMap) throws ReportingServiceException {
        List<ListValueTypeFilter> filterTypes = new ArrayList<>();
        filterTypes.addAll(processor.getListValueTypeFilters());

        if (!reportType.equals(ReportTypeEnum.ALL)) { // If report type is all then assets are already fetched for VMS data
            assetMap = assetModule.getAssetMap(processor);
        }

        if (assetMap != null) {
            Collection<Asset> assets = assetMap.values();
            List<String> assetName = new ArrayList<String>();
            for (Asset asset : assets) {
                assetName.add(asset.getName());
            }
            filterTypes.add(new ListValueTypeFilter(SearchFilter.VESSEL_NAME, assetName));
        }

        return filterTypes;
    }


    private String getFilterAreaWkt(FilterProcessor processor, List<AreaIdentifierType> scopeAreaIdentifierList) throws ReportingServiceException {
        final Set<AreaIdentifierType> areaIdentifierList = processor.getAreaIdentifierList();
        if (isNotEmpty(areaIdentifierList) || isNotEmpty(scopeAreaIdentifierList)) {
            HashSet<AreaIdentifierType> areaIdentifierTypes = null;
            if (isNotEmpty(scopeAreaIdentifierList)){
                areaIdentifierTypes = new HashSet<>(scopeAreaIdentifierList);
            }
            return spatialModule.getFilterArea(areaIdentifierTypes, areaIdentifierList);
        }
        return null;
    }
}