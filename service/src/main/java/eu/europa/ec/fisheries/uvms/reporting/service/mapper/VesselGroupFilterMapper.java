package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VesselGroupFilterMapper {

    VesselGroupFilterMapper INSTANCE = Mappers.getMapper(VesselGroupFilterMapper.class);

    @Mapping(target = "reportId", source = "report.id")
    VesselGroupFilterDTO vesselGroupFilterToVesselGroupFilterDTO(VesselGroupFilter vesselGroupFilter);

    VesselGroupFilter vesselGroupFilterDTOToVesselGroupFilter(VesselGroupFilterDTO vesselGroupFilterDTO);

}
