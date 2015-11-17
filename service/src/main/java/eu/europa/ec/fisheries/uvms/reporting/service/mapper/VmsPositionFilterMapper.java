package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface VmsPositionFilterMapper {

    VmsPositionFilterMapper INSTANCE = Mappers.getMapper(VmsPositionFilterMapper.class);

    VmsPositionFilterDTO vmsPositionFilterToVmsPositionFilterDTO(VmsPositionFilter vmsPositionFilter);

    VmsPositionFilter vmsPositionFilterDTOToVmsPositionFilter(VmsPositionFilterDTO vmsPositionFilterDTO);

}
