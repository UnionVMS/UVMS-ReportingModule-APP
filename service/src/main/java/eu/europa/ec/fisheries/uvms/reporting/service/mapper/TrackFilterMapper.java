package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DistanceRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TimeRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class TrackFilterMapper {

    static final public TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);

    abstract public TrackFilterDTO trackFilterToTrackFilterDTO(VmsTrackFilter vmsTrackFilter);

    public VmsTrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO trackFilterDTO) {
        return VmsTrackFilter.builder()
                .id(trackFilterDTO.getId())
                .reportId(trackFilterDTO.getReportId())
                .timeRange(new TimeRange(trackFilterDTO.getMinTime(), trackFilterDTO.getMaxTime()))
                .durationRange(new DurationRange(trackFilterDTO.getMinDuration(), trackFilterDTO.getMaxDuration()))
                .distanceRange(new DistanceRange(trackFilterDTO.getMinDistance(), trackFilterDTO.getMaxDistance()))
                .minAvgSpeed(trackFilterDTO.getMinAvgSpeed())
                .maxAvgSpeed(trackFilterDTO.getMaxAvgSpeed())
                .build();
    }
}
