package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class MapConfigMapper {

    public static MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    @Mappings({
            @Mapping(target = "coordinatesFormat", expression = "java(convertCoordinatesFormatToString(mapConfigurationType.getCoordinatesFormat()))"),
            @Mapping(target = "scaleBarUnits", expression = "java(convertScaleBarUnitsToString(mapConfigurationType.getScaleBarUnits()))")
    })
    public abstract MapConfigurationDTO mapConfigurationTypeToMapConfigurationDTO(MapConfigurationType mapConfigurationType);

    protected String convertCoordinatesFormatToString(CoordinatesFormat coordinatesFormat) {
        if (coordinatesFormat != null) {
            return coordinatesFormat.value();
        }
        return null;
    }

    protected String convertScaleBarUnitsToString(ScaleBarUnits scaleBarUnits) {
        if (scaleBarUnits != null) {
            return scaleBarUnits.value();
        }
        return null;
    }
}
