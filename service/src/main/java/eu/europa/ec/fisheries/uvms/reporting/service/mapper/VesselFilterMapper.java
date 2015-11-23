package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface VesselFilterMapper {

    VesselFilterMapper INSTANCE = Mappers.getMapper(VesselFilterMapper.class);

    VesselFilterDTO vesselFilterToVesselFilterDTO(VesselFilter vesselFilter);

    VesselFilter vesselFilterDTOToVesselFilter(VesselFilterDTO vesselFilterDTO);

    @Mappings({
            @Mapping(source = "guid", target = "value"),
            @Mapping(constant = "GUID", target = "key"),
    })
    VesselListCriteriaPair vesselFilterToVesselListCriteriaPair(VesselFilter vesselFilter);

    @Mappings({
            @Mapping(source = "guid", target = "value"),
            @Mapping(constant = "CONNECT_ID", target = "key"),
    })
    ListCriteria vesselFilterToListCriteria(VesselFilter vesselFilter);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VesselFilter incoming, @MappingTarget VesselFilter current);

}
