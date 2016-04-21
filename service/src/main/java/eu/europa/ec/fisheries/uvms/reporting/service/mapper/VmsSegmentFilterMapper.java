package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.model.vms.VmsSegment;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class, imports = {DurationRange.class, SegmentCategoryType.class, SearchKey.class})
public interface VmsSegmentFilterMapper {

    VmsSegmentFilterMapper INSTANCE = Mappers.getMapper(VmsSegmentFilterMapper.class);

    @Mappings({
            @Mapping(target = "minDuration", source = "vmsSegmentFilter.durationRange.minDuration"),
            @Mapping(target = "maxDuration", source = "vmsSegmentFilter.durationRange.maxDuration")
    })
    VmsSegmentFilterDTO vmsSegmentFilterToVmsSegmentFilterDTO(VmsSegmentFilter vmsSegmentFilter);

    @Mapping(target = "durationRange", expression = "java(new DurationRange(dto.getMinDuration(), dto.getMaxDuration()))")
    VmsSegmentFilter vmsSegmentFilterDTOToVmsSegmentFilter(VmsSegmentFilterDTO dto);


    @Mappings({
            @Mapping(source = "segMaxSpeed", target = "maximumSpeed"),
            @Mapping(source = "segMinSpeed", target = "minimumSpeed"),
            @Mapping(target = "category", expression = "java(dto.getSegCategory() != null ? Enum.valueOf( SegmentCategoryType.class, dto.getSegCategory()) : null)"),
            @Mapping(target = "durationRange", expression = "java(new DurationRange(dto.getSegMinDuration(), dto.getSegMaxDuration()))")
    })
    VmsSegmentFilter vmsSegmentToVmsSegmentFilter(VmsSegment dto);

    @Mappings({
            @Mapping(constant = "SEGMENT_SPEED", target = "key"),
            @Mapping(source = "minimumSpeed", target = "from", defaultValue = "0"),
            @Mapping(source = "maximumSpeed", target = "to", defaultValue = "9223372036854775807")
    })
    RangeCriteria speedRangeToRangeCriteria(VmsSegmentFilter segmentFilter);

    @Mappings({
            @Mapping(constant = "CATEGORY", target = "key"),
            @Mapping(source = "category", target = "value")
    })
    ListCriteria categoryToListCriteria(VmsSegmentFilter filter);

    @Mappings({
            @Mapping(constant = "SEGMENT_DURATION", target = "key"),
            @Mapping(source = "durationRange.minDuration", target = "from", defaultValue = "0"),
            @Mapping(source = "durationRange.maxDuration", target = "to", defaultValue = "9223372036854775807")
    })
    RangeCriteria durationRangeToRangeCriteria(VmsSegmentFilter segmentFilter);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VmsSegmentFilter incoming, @MappingTarget VmsSegmentFilter current);
}
