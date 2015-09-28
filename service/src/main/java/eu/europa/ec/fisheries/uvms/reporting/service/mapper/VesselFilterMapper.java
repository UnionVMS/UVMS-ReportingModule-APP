package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VesselFilterMapper {

    VesselFilterMapper INSTANCE = Mappers.getMapper(VesselFilterMapper.class);

    @Mapping(target = "reportId", source = "report.id")
    VesselFilterDTO vesselFilterToVesselFilterDTO(VesselFilter vesselFilter);

    VesselFilter vesselFilterDTOToVesselFilter(VesselFilterDTO vesselFilterDTO);

}
