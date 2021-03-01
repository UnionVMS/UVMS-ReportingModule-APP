package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.commons.domain.Range;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.MovementRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportingDataService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.joda.time.DateTime;

public class ReportingDataServiceImpl implements ReportingDataService {

    @Inject
    private MovementRepository movementRepository;

    @Override
    public ExecutionResultDTO executeReport(Report report, DateTime now,
                                            List<AreaIdentifierType> areaRestrictions,
                                            Boolean withActivity,
                                            DisplayFormat displayFormat,
                                            Long pageNumber,
                                            Long pageSize) {

        long offset = pageNumber != null ? pageNumber * pageSize : 0;
        long limit = pageSize != null ? pageSize : 1000;

        StringBuilder query =
                new StringBuilder()
                        .append("SELECT DISTINCT m.id, m.movement_guid ")
                        .append("FROM reporting.movement m ")
                        .append("JOIN reporting.segment s ON s.movement_guid = m.movement_guid ")
                        .append("JOIN reporting.track t ON t.id = s.track_id ")
                        .append("JOIN reporting.asset a ON a.asset_hist_guid = m.connect_id ");

        List<Filter> areaFilters = report.getFilters().stream()
                .filter(f -> FilterType.areas.equals(f.getType()))
                .collect(Collectors.toList());

        updateQueryWithJoinForAreaCriteria(query,areaFilters);

        Optional<Filter> dateFilter = report.getFilters().stream().filter(f -> FilterType.common.equals(f.getType())).findFirst();
        dateFilter.ifPresent(dF -> {
            String startDate = ((CommonFilter) dF).getDateRange().getStartDate().toInstant().toString().replace("T", " ").replace("Z", " ");
            String endDate = ((CommonFilter) dF).getDateRange().getEndDate().toInstant().toString().replace("T", " ").replace("Z", " ");
            query.append("WHERE m.position_time >= '" + startDate + "' AND m.position_time  <= '" + endDate + "' ");
        });

        updateQueryWithAreaCriteria(query, areaFilters);

        List<Filter> filters = report.getFilters().stream()
                .filter(f -> !Arrays.asList(FilterType.fa, FilterType.common, FilterType.asset, FilterType.areas).contains(f.getType()))
                .collect(Collectors.toList());

        for (Filter f : filters) {

            if (FilterType.vmstrack.equals(f.getType())) {
                updateQueryWithTrackCriteria(query, (VmsTrackFilter) f);
            } else if (FilterType.vmsseg.equals(f.getType())) {
                updateQueryWithSegmentCriteria(query, (VmsSegmentFilter) f);
            }
        }

        List<String> assets = report.getFilters().stream().filter(f -> FilterType.asset.equals(f.getType())).map(f -> ((AssetFilter) f).getGuid()).collect(Collectors.toList());
        updateQueryWithAssetCriteria(query, assets);

        query.append("ORDER BY m.id DESC ");
        query.append("OFFSET " + offset + " LIMIT " + limit);

        movementRepository.executeQuery(query.toString());
        return new ExecutionResultDTO();
    }

    private void updateQueryWithSegmentCriteria(StringBuilder query, VmsSegmentFilter f) {
        DurationRange durationRange = f.getDurationRange();
        if (durationRange != null) {
            if (durationRange.getMinDuration() != null && durationRange.getMaxDuration() != null) {
                query.append("AND s.duration >= " + durationRange.getMinDuration() + " AND s.duration <= " + durationRange.getMaxDuration() + " ");
            } else if (durationRange.getMinDuration() != null && durationRange.getMaxDuration() == null) {
                query.append("AND s.duration >= " + durationRange.getMinDuration() + " ");
            } else if (durationRange.getMinDuration() == null && durationRange.getMaxDuration() != null) {
                query.append("AND s.duration <= " + durationRange.getMaxDuration() + " ");
            }
        }
        SegmentCategoryType category = f.getCategory();
        if (category != null){
            SegmentCategoryType[] values = SegmentCategoryType.values();
            query.append("AND s.segment_category = '" + values[category.ordinal()] + "' ");
        }

        if (f.getMinimumSpeed() != null && f.getMaximumSpeed() != null) {
            query.append("AND s.speed_over_ground >= " + f.getMinimumSpeed() + " AND s.speed_over_ground <= " +f.getMaximumSpeed() + " ");
        } else if (f.getMinimumSpeed() != null && f.getMaximumSpeed() == null) {
            query.append("AND s.speed_over_ground >= " + f.getMinimumSpeed() + " ");
        } else if (f.getMinimumSpeed() == null && f.getMaximumSpeed() != null) {
            query.append("AND s.speed_over_ground <= " + f.getMaximumSpeed() + " ");
        }
    }

    private void updateQueryWithJoinForAreaCriteria(StringBuilder query, List<Filter> areaFilters) {
        if (areaFilters.size() > 0) {
            query.append("join reporting.movement_area ma on ma.movement_id = m.id ")
                    .append("join reporting.area ra on ra.id = ma.area_id ");
        }
    }

    private void updateQueryWithAreaCriteria(StringBuilder query, List<Filter> areaFilters) {
        boolean firstAreaFilter = true;
        query.append("AND ");
        query.append("( ");
        for (Filter f : areaFilters) {
            if (firstAreaFilter) {
                query.append("(ra.area_remote_id = '" + f.getAreaIdentifierType().getId() + "' AND ra.area_type = '" + f.getAreaIdentifierType().getAreaType() + "') ");

            } else {
                query.append("OR (ra.area_remote_id = '" + f.getAreaIdentifierType().getId() + "' AND ra.area_type = '" + f.getAreaIdentifierType().getAreaType() + "') ");
            }
            firstAreaFilter = false;
        }
        query.append(") ");
    }

    private void updateQueryWithAssetCriteria(StringBuilder query, List<String> assets) {
        if (assets.size() > 0) {
            query.append("AND ");
            query.append("a.asset_hist_guid in ( ");
            for (int i = 0; i < assets.size(); i++) {
                query.append("'" + assets.get(i) + "'" + (i == assets.size() - 1 ? "" : ","));
            }
            query.append(") ");
        }
    }

    private void updateQueryWithTrackCriteria(StringBuilder query, VmsTrackFilter f) {
        VmsTrackFilter trackFilter = f;
        Range timeRange = trackFilter.getTimeRange();
        boolean hasTimeRange = false;
        if (timeRange != null) {
            if (timeRange.getMin() != null && timeRange.getMax() != null) {
                hasTimeRange = true;
                query.append("AND t.duration >= " + timeRange.getMin() + " AND t.duration <= " + timeRange.getMax() + " ");
            } else if (timeRange.getMin() != null && timeRange.getMax() == null) {
                hasTimeRange = true;
                query.append("AND t.duration >= " + timeRange.getMin() + " ");
            } else if (timeRange.getMin() == null && timeRange.getMax() != null) {
                hasTimeRange = true;
                query.append("AND t.duration <= " + timeRange.getMax() + " ");
            }
        }

        DurationRange durationRange = trackFilter.getDurationRange();
        if (durationRange != null) {
            if (durationRange.getMinDuration() != null && durationRange.getMinDuration() != null) {
                if (hasTimeRange) {
                    query.append(" AND ");
                }
                query.append("t.total_time_at_sea >= " + durationRange.getMinDuration() + " and t.total_time_at_sea <= " + durationRange.getMaxDuration() + " ");
            } else if (durationRange.getMinDuration() != null && durationRange.getMinDuration() == null) {
                if (hasTimeRange) {
                    query.append(" AND ");
                }
                query.append("t.total_time_at_sea >= " + durationRange.getMinDuration() + " ");
            } else if (durationRange.getMinDuration() == null && durationRange.getMinDuration() != null) {
                if (hasTimeRange) {
                    query.append(" AND ");
                }
                query.append("t.total_time_at_sea <= " + durationRange.getMaxDuration() + " ");
            }
        }
    }
}
