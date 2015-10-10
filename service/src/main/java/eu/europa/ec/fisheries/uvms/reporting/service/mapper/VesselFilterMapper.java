package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface VesselFilterMapper {

    VesselFilterMapper INSTANCE = Mappers.getMapper(VesselFilterMapper.class);

    VesselFilterDTO vesselFilterToVesselFilterDTO(VesselFilter vesselFilter);

    VesselFilter vesselFilterDTOToVesselFilter(VesselFilterDTO vesselFilterDTO);

}
