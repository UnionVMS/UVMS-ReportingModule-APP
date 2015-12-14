package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface VmsPositionFilterMapper {

    VmsPositionFilterMapper INSTANCE = Mappers.getMapper(VmsPositionFilterMapper.class);

    VmsPositionFilterDTO vmsPositionFilterToVmsPositionFilterDTO(VmsPositionFilter vmsPositionFilter);

    VmsPositionFilter vmsPositionFilterDTOToVmsPositionFilter(VmsPositionFilterDTO vmsPositionFilterDTO);

    @Mappings({
            @Mapping(constant = "MOVEMENT_SPEED", target = "key"),
            @Mapping(source = "minimumSpeed", target = "from", defaultValue = "0"),
            @Mapping(source = "maximumSpeed", target = "to", defaultValue = "1000")
    })
    RangeCriteria speedRangeToRangeCriteria(VmsPositionFilter vmsPositionFilter);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VmsPositionFilter incoming, @MappingTarget VmsPositionFilter current);
}
