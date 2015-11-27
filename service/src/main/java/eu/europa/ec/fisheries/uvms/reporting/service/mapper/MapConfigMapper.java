package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapConfigMapper {

    MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    MapConfigurationType configAndReportToMapConfigurationType(MapConfigurationDTO incoming);

}
