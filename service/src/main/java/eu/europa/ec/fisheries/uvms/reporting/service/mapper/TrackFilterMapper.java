package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TrackFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface TrackFilterMapper {

    TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);

    TrackFilterDTO trackFilterToTrackFilterDTO(TrackFilter trackFilter);

    TrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO trackFilterDTO);

}
