package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.vms.VmsTrack;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DistanceRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TimeRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {DurationRange.class, DistanceRange.class, TimeRange.class})
public interface TrackFilterMapper {

   TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);

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
            @Mapping(target = "durationRange", expression = "java(new DurationRange(Float.valueOf(dto.getTrkMinDuration()), Float.valueOf(dto.getTrkMaxDuration())))"),
            @Mapping(target = "timeRange", expression = "java(new TimeRange(Float.valueOf(dto.getTrkMinTime()), Float.valueOf(dto.getTrkMaxTime())))")
    })
    VmsTrackFilter tracksToVmsTrackFilter(VmsTrack dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VmsTrackFilter incoming, @MappingTarget VmsTrackFilter current);
}
