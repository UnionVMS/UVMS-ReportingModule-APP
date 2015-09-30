package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface DateTimeFilterMapper {

    DateTimeFilterMapper INSTANCE = Mappers.getMapper(DateTimeFilterMapper.class);

    @Mapping(target = "reportId", source = "report.id")
    PositionFilterDTO positionFilterToPositionFilterDTO(DateTimeFilter positionFilter);

    DateTimeFilter positionFilterDTOToPositionFilter(PositionFilterDTO positionFilterDTO);

}
