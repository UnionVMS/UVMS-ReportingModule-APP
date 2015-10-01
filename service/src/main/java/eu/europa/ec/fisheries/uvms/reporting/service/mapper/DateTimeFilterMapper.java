package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.DateTimeFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ObjectFactory.class, PositionSelectorMapper.class})
public interface DateTimeFilterMapper {

    DateTimeFilterMapper INSTANCE = Mappers.getMapper(DateTimeFilterMapper.class);

    @Mapping(target = "reportId", source = "report.id")
    DateTimeFilterDTO dateTimeFilterToDateTimeFilterDTO(DateTimeFilter dateTimeFilter);

    DateTimeFilter dateTimeFilterDTOToDateTimeFilter(DateTimeFilterDTO dateTimeFilterDTO);

}
