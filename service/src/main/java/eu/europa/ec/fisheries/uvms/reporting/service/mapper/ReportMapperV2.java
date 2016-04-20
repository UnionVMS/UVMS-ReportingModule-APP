package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.HashSet;
import java.util.Set;

@Mapper(imports = {ReportDetails.class, Report.class, DateUtils.class, Audit.class, VisibilityEnum.class}, uses = {ObjectFactory.class})
public abstract class ReportMapperV2 {

    public static ReportMapperV2 INSTANCE = Mappers.getMapper(ReportMapperV2.class);

    @Mappings({
        @Mapping(target = "visibility", expression = "java(VisibilityEnum.getByName(dto.getVisibility()))"),
        @Mapping(target = "audit", expression = "java(new Audit(dto.getCreatedOn() != null ? DateUtils.UI_FORMATTER.parseDateTime(dto.getCreatedOn()).toDate() : null))"),
        @Mapping(target = "details", expression = "java(new ReportDetails(dto.getDesc(), dto.getName(), dto.isWithMap(), null, dto.getCreatedBy()))"),
        @Mapping(target = "filters", expression = "java(mapFilters(dto))"),
    })
    public abstract Report reportDtoToReport(eu.europa.ec.fisheries.uvms.reporting.model.vms.Report dto);

    Set<Filter> mapFilters(eu.europa.ec.fisheries.uvms.reporting.model.vms.Report dto){

        Set<Filter> filterSet = new HashSet<>();

        filterSet.addAll(AreaFilterMapper.INSTANCE.arealistToAreaFilterSet(dto.getFilterExpression().getAreas()));
        filterSet.add(CommonFilterMapper.INSTANCE.commonToCommonFilter(dto.getFilterExpression().getCommon()));
        filterSet.addAll(AssetFilterMapper.INSTANCE.assetListToAssetFilterSet(dto.getFilterExpression().getAssets()));

        if (dto.getFilterExpression().getVms() != null){
            filterSet.add(VmsTrackFilterMapper.INSTANCE.tracksToVmsTrackFilter(dto.getFilterExpression().getVms().getVmstrack()));
            filterSet.add(VmsPositionFilterMapper.INSTANCE.vmsPositionToVmsPositionFilter(dto.getFilterExpression().getVms().getVmsposition()));
            filterSet.add(VmsSegmentFilterMapper.INSTANCE.vmsSegmentToVmsSegmentFilter(dto.getFilterExpression().getVms().getVmssegment()));
        }

        return filterSet;
    }
}
