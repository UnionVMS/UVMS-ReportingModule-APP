package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ObjectFactory.class, PositionSelectorMapper.class})
public interface DateTimeFilterMapper {

    DateTimeFilterMapper INSTANCE = Mappers.getMapper(DateTimeFilterMapper.class);

    CommonFilterDTO dateTimeFilterToDateTimeFilterDTO(CommonFilter dateTimeFilter);

    CommonFilter dateTimeFilterDTOToDateTimeFilter(CommonFilterDTO dateTimeFilterDTO);

}
