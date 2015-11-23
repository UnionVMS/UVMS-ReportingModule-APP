package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface VesselGroupFilterMapper {

    VesselGroupFilterMapper INSTANCE = Mappers.getMapper(VesselGroupFilterMapper.class);

    VesselGroupFilterDTO vesselGroupFilterToVesselGroupFilterDTO(VesselGroupFilter vesselGroupFilter);

    VesselGroupFilter vesselGroupFilterDTOToVesselGroupFilter(VesselGroupFilterDTO vesselGroupFilterDTO);

    @Mappings({
            @Mapping(constant = "true", target = "dynamic"),
            @Mapping(source = "userName", target = "user"),
    })
    VesselGroup vesselGroupFilterToVesselGroup(VesselGroupFilter vesselGroupFilter);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VesselGroupFilter incoming, @MappingTarget VesselGroupFilter current);
}
