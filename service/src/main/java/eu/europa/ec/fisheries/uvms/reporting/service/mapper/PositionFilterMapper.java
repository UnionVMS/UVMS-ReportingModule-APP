package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface PositionFilterMapper {

    PositionFilterMapper INSTANCE = Mappers.getMapper(PositionFilterMapper.class);

    @Mapping(target = "reportId", source = "report.id")
    PositionFilterDTO positionFilterToPositionFilterDTO(PositionFilter positionFilter);

    PositionFilter positionFilterDTOToPositionFilter(PositionFilterDTO positionFilterDTO);

}
