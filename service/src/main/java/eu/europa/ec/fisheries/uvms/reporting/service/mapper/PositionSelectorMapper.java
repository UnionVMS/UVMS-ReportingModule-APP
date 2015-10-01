package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PositionSelectorMapper {

    PositionSelectorMapper INSTANCE = Mappers.getMapper(PositionSelectorMapper.class);

    PositionSelectorDTO positionSelectorToPositionSelectorDTO(PositionSelector positionSelector);

    PositionSelector positionSelectorDTOToPositionSelector(PositionSelectorDTO positionSelector);

}
