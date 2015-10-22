package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface TrackFilterMapper {

    TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);

    TrackFilterDTO trackFilterToTrackFilterDTO(VmsTrackFilter vmsTrackFilter);

    VmsTrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO trackFilterDTO);

}
