/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Constants.ADDITIONAL_PROPERTIES;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Constants.TIMESTAMP;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FACatchSummaryReportResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripIdWithGeometry;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.GroupCriteria;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ListValueTypeFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AuditService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportExecutionService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FACatchSummaryDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FishingActivitySummaryDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TripDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.GroupCriteriaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.comparator.GroupCriteriaFilterSequenceComparator;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.GroupCriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FACatchSummaryMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FishingActivityMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FishingTripMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.GroupCriteriaFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapperV2;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Stateless
@Local(value = ReportExecutionService.class)
@Slf4j
@Interceptors(TracingInterceptor.class)
public class ReportExecutionServiceBean implements ReportExecutionService {


    @EJB
    private ReportRepository repository;

    @EJB
    private AuditService auditService;

    @EJB
    private AssetServiceBean assetModule;

    @EJB
    private MovementServiceBean movementModule;

    @EJB
    private SpatialService spatialModule;

    @EJB
    private ActivityService activityService;

    @Override
    @Transactional
    @IAuditInterceptor(auditActionType = AuditActionEnum.EXECUTE)
    public ExecutionResultDTO getReportExecutionByReportId(final Long id, final String username, final String scopeName, final List<AreaIdentifierType> areaRestrictions, final DateTime now, Boolean isAdmin, Boolean withActivity) throws ReportingServiceException {
        log.debug("[START] getReportExecutionByReportId({}, {}, {})", username, scopeName, id);
        Report reportByReportId = repository.findReportByReportId(id, username, scopeName, isAdmin);
        if (reportByReportId == null) {
            final String error = "No report found with id " + id;
            log.error(error);
            throw new ReportingServiceException(error);
        }
        ExecutionResultDTO resultDTO = executeReport(reportByReportId, now, areaRestrictions, withActivity);
        reportByReportId.updateExecutionLog(username);
        log.debug("[END] getReportExecutionByReportId(...)");
        return resultDTO;
    }

    @Override
    public ExecutionResultDTO getReportExecutionWithoutSave(final eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report report, final List<AreaIdentifierType> areaRestrictions, String userName, Boolean withActivity) throws ReportingServiceException {
        Map additionalProperties = (Map) report.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);
        DateTime dateTime = DateUtils.UI_FORMATTER.parseDateTime((String) additionalProperties.get(TIMESTAMP));
        Report toReport = ReportMapperV2.INSTANCE.reportDtoToReport(report);
        ExecutionResultDTO resultDTO = executeReport(toReport, dateTime, areaRestrictions, withActivity);
        auditService.sendAuditReport(AuditActionEnum.EXECUTE, report.getName(), userName);
        return resultDTO;
    }

    private ExecutionResultDTO executeReport(Report report, DateTime dateTime, List<AreaIdentifierType> areaRestrictions, Boolean userActivityAllowed) throws ReportingServiceException {

        try {

            Set<Filter> filters = report.getFilters();
            FilterProcessor processor = new FilterProcessor(report.getFilters(), dateTime);
            ExecutionResultDTO resultDTO = new ExecutionResultDTO();
            String wkt = getFilterAreaWkt(processor, areaRestrictions);
            boolean hasAssets = processor.hasAssetsOrAssetGroups();

            List<SingleValueTypeFilter> singleValueTypeFilters = extractSingleValueFilters(processor, wkt);
            List<ListValueTypeFilter> listValueTypeFilters = extractListValueFilters(processor, resultDTO.getAssetMap(), hasAssets);

            if (ReportTypeEnum.STANDARD == report.getReportType()) {

                fetchPositionalData(resultDTO, processor, wkt);

                if (userActivityAllowed && !report.isLastPositionSelected()) {
                    FishingTripResponse tripResponse = activityService.getFishingTrips(singleValueTypeFilters, listValueTypeFilters);
                    for (FishingTripIdWithGeometry fishingTripIdWithGeometry : tripResponse.getFishingTripIdLists()) {
                        TripDTO trip = FishingTripMapper.INSTANCE.fishingTripToTripDto(fishingTripIdWithGeometry);
                        updateTripWithVmsPositionCount(trip, resultDTO.getMovementMap());
                        resultDTO.getTrips().add(trip);
                    }
                    List<FishingActivitySummaryDTO> activitySummaryDTOs = new ArrayList<>();
                    for(FishingActivitySummary summary: tripResponse.getFishingActivityLists()){
                        activitySummaryDTOs.add(FishingActivityMapper.INSTANCE.activitySummaryToFishingActivitySummaryDTO(summary));
                    }
                    resultDTO.setActivityList(activitySummaryDTOs);
                }
            } else if (ReportTypeEnum.SUMMARY == report.getReportType()) {

                if (userActivityAllowed) {

                    List<GroupCriteria> groupCriteriaList = extractGroupCriteriaList(filters);
                    FACatchSummaryReportResponse faCatchSummaryReport =
                            activityService.getFaCatchSummaryReport(singleValueTypeFilters, listValueTypeFilters, groupCriteriaList);

                    FACatchSummaryDTO faCatchSummaryDTO = FACatchSummaryMapper.mapToFACatchSummaryDTO(faCatchSummaryReport);
                    resultDTO.setFaCatchSummaryDTO(faCatchSummaryDTO);
                }
            }

            return resultDTO;

        } catch (ProcessorException e) {
            String error = "Error while processing reporting filters";
            log.error(error, e);
            throw new ReportingServiceException(error, e);
        }
    }

    private void updateTripWithVmsPositionCount(TripDTO trip, Collection<MovementMapResponseType> movementMap) {
        Integer count = 0;

        if (trip != null && (trip.getRelativeFirstFaDateTime() != null && trip.getRelativeLastFaDateTime() != null)) {
            for (MovementMapResponseType map : movementMap) {
                for (MovementType movement : map.getMovements()) {
                    if (movement.getPositionTime() != null) {
                        Date movementDate = movement.getPositionTime().toGregorianCalendar().getTime();
                        if (movementDate.after(trip.getRelativeFirstFaDateTime()) && movementDate.before(trip.getRelativeLastFaDateTime())) {
                            count++;
                        }
                    }
                }
            }
        }
        trip.setVmsPositionCount(count);
    }

    private List<GroupCriteria> extractGroupCriteriaList(Set<Filter> filters) {

        ImmutableList<GroupCriteriaFilter> groupCriteriaFilters = FluentIterable.from(filters)
                .filter(GroupCriteriaFilter.class)
                .toSortedList(new GroupCriteriaFilterSequenceComparator());

        List<GroupCriteriaType> types = new ArrayList<>();

        for (GroupCriteriaFilter filter : groupCriteriaFilters) {
            types.addAll(filter.getValues());
        }

        return GroupCriteriaFilterMapper.INSTANCE.mapGroupCriteriaTypeListToGroupCriteriaList(types);
    }

    private void fetchPositionalData(ExecutionResultDTO resultDTO, FilterProcessor processor, String wkt) throws ReportingServiceException {

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

    private List<SingleValueTypeFilter> extractSingleValueFilters(FilterProcessor processor, String areaWkt) {
        List<SingleValueTypeFilter> filterTypes = new ArrayList<>();
        filterTypes.addAll(processor.getSingleValueTypeFilters()); // Add all the Fa filter criteria from the filters

        log.info("generate filtered area WKT by combining area filters and scope area");
        if (areaWkt != null && !areaWkt.isEmpty()) {
            filterTypes.add(new SingleValueTypeFilter(SearchFilter.AREA_GEOM, areaWkt));
        }
        return filterTypes;
    }

    private List<ListValueTypeFilter> extractListValueFilters(FilterProcessor processor, Map<String, Asset> assetMap, Boolean isAssetsExist) throws ReportingServiceException {
        List<ListValueTypeFilter> filterTypes = new ArrayList<>();
        filterTypes.addAll(processor.getListValueTypeFilters());

        if (isAssetsExist && assetMap != null) {
            Collection<Asset> assets = assetMap.values();
            List<String> assetName = new ArrayList<>();
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
            if (isNotEmpty(scopeAreaIdentifierList)) {
                areaIdentifierTypes = new HashSet<>(scopeAreaIdentifierList);
            }
            return spatialModule.getFilterArea(areaIdentifierTypes, areaIdentifierList);
        }
        return null;
    }
}