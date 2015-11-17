package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DistanceRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TimeRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class TrackFilterMapper {

    static final public TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);

    @Mappings({
            @Mapping(source = "durationRange.minDuration", target = "minDuration"),
            @Mapping(source = "durationRange.maxDuration", target = "maxDuration"),
            @Mapping(source = "distanceRange.minDistance", target = "minDistance"),
            @Mapping(source = "distanceRange.maxDistance", target = "maxDistance"),
            @Mapping(source = "timeRange.minTime", target = "minTime"),
            @Mapping(source = "timeRange.maxTime", target = "maxTime")
    })
    abstract public TrackFilterDTO trackFilterToTrackFilterDTO(VmsTrackFilter vmsTrackFilter);

    public VmsTrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO dto) {
        return VmsTrackFilter.builder()
                .id(dto.getId())
                .reportId(dto.getReportId())
                .timeRange(new TimeRange(dto.getMinTime(), dto.getMaxTime()))
                .durationRange(new DurationRange(dto.getMinDuration(), dto.getMaxDuration()))
                .distanceRange(new DistanceRange(dto.getMinDistance(), dto.getMaxDistance()))
                .minAvgSpeed(dto.getMinAvgSpeed())
                .maxAvgSpeed(dto.getMaxAvgSpeed())
                .build();
    }
}
