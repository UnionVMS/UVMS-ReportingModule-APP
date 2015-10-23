package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class TrackFilterMapper {

    abstract public TrackFilterDTO trackFilterToTrackFilterDTO(VmsTrackFilter vmsTrackFilter);

    public VmsTrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO trackFilterDTO) {
        return VmsTrackFilter.TrackFilterBuilder()
                .id(trackFilterDTO.getId())
                .reportId(trackFilterDTO.getReportId())
                .minTime(trackFilterDTO.getMinTime())
                .maxTime(trackFilterDTO.getMaxTime())
                .minDuration(trackFilterDTO.getMinDuration())
                .maxDuration(trackFilterDTO.getMaxDuration())
                .build();
    }
}
