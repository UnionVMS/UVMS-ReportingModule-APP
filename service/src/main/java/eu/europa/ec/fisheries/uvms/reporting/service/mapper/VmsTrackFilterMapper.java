package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.uvms.reporting.model.vms.VmsTrack;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {DurationRange.class, DistanceRange.class, TimeRange.class, RangeKeyType.class})
public interface VmsTrackFilterMapper {

   VmsTrackFilterMapper INSTANCE = Mappers.getMapper(VmsTrackFilterMapper.class);

    @Mappings({
            @Mapping(source = "durationRange.minDuration", target = "minDuration"),
            @Mapping(source = "durationRange.maxDuration", target = "maxDuration"),
            @Mapping(source = "timeRange.minTime", target = "minTime"),
            @Mapping(source = "timeRange.maxTime", target = "maxTime")
    })
    TrackFilterDTO trackFilterToTrackFilterDTO(VmsTrackFilter vmsTrackFilter);

    @Mappings({
            @Mapping(target = "durationRange", expression = "java(new DurationRange(dto.getMinDuration(), dto.getMaxDuration()))"),
            @Mapping(target = "timeRange", expression = "java(new TimeRange(dto.getMinTime(), dto.getMaxTime()))")
    })
    VmsTrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO dto); // TODO refactor with Tracks

    @Mappings({
            @Mapping(constant = "TRACK_SPEED", target = "key"),
            @Mapping(source = "minAvgSpeed", target = "from", defaultValue = "0"),
            @Mapping(source = "maxAvgSpeed", target = "to", defaultValue = "9223372036854775807")
    })
    RangeCriteria speedRangeToRangeCriteria(VmsTrackFilter trackFilter);

    @Mappings({
            @Mapping(constant = "TRACK_DURATION", target = "key"),
            @Mapping(source = "durationRange.minDuration", target = "from", defaultValue = "0"),
            @Mapping(source = "durationRange.maxDuration", target = "to", defaultValue = "9223372036854775807")
    })
    RangeCriteria durationRangeToRangeCriteria(VmsTrackFilter trackFilter);

    @Mappings({
            @Mapping(constant = "TRACK_DURATION_AT_SEA", target = "key"),
            @Mapping(source = "timeRange.minTime", target = "from", defaultValue = "0"),
            @Mapping(source = "timeRange.maxTime", target = "to", defaultValue = "9223372036854775807")
    })
    RangeCriteria timeRangeToRangeCriteria(VmsTrackFilter trackFilter);

    @Mappings({
            @Mapping(target = "durationRange", expression = "java(new DurationRange(dto.getTrkMinDuration(), dto.getTrkMaxDuration()))"),
            @Mapping(target = "timeRange", expression = "java(new TimeRange(dto.getTrkMinTime(), dto.getTrkMaxTime()))")
    })
    VmsTrackFilter tracksToVmsTrackFilter(VmsTrack dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VmsTrackFilter incoming, @MappingTarget VmsTrackFilter current);
}
