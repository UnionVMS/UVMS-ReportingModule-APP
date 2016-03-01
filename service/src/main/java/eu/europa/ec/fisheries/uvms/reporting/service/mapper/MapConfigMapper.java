package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public abstract class MapConfigMapper {

    public static MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    @Mappings({
            @Mapping(target = "coordinatesFormat", expression = "java(convertCoordinatesFormatToString(mapConfigurationType.getCoordinatesFormat()))"),
            @Mapping(target = "scaleBarUnits", expression = "java(convertScaleBarUnitsToString(mapConfigurationType.getScaleBarUnits()))"),
            @Mapping(target = "visibilitySettings", ignore = true),
            @Mapping(target = "styleSettings", ignore = true)
    })
    public abstract MapConfigurationDTO mapConfigurationTypeToMapConfigurationDTO(MapConfigurationType mapConfigurationType);

    @Mappings({
            @Mapping(source = "positions", target = "visibilityPositionsDto"),
            @Mapping(source = "segments", target = "visibilitySegmentDto"),
            @Mapping(source = "tracks", target = "visibilityTracksDto")
    })
    public abstract VisibilitySettingsDto getVisibilitySettingsDto(VisibilitySettingsType visibilitySettingsType);

    @Mappings({
            @Mapping(source = "visibilityPositionsDto", target = "positions"),
            @Mapping(source = "visibilitySegmentDto", target = "segments"),
            @Mapping(source = "visibilityTracksDto", target = "tracks")
    })
    public abstract VisibilitySettingsType getVisibilitySettingsType(VisibilitySettingsDto visibilitySettingsDto);

    @Mappings({
            @Mapping(source = "orders", target = "order"),
            @Mapping(target = "isAttributeVisible", expression = "java(getAttributeValue(visibilityAttributeType.isIsAttributeVisible()))")
    })
    public abstract VisibilityAttributesDto getVisibilityAttributeDto(VisibilityAttributeType visibilityAttributeType);

    public abstract VisibilityPositionsDto getVisibilityPositionDto(VisibilityPositionsType visibilityPositionsType);

    public abstract VisibilitySegmentDto getVisibilitySegmentDto(VisibilitySegmentType visibilitySegmentType);

    public abstract VisibilityTracksDto getVisibilityTrackDto(VisibilityTracksType visibilityTracksType);


    @Mappings({
            @Mapping(source = "order", target = "orders"),
            @Mapping(target = "isAttributeVisible", expression = "java(getAttributeValue(visibilityAttributeDto.isAttributeVisible()))")
    })
    public abstract VisibilityAttributeType getVisibilityAttributeType(VisibilityAttributesDto visibilityAttributeDto);

    public abstract VisibilityPositionsType getVisibilityPositionType(VisibilityPositionsDto visibilityPositionsDto);

    public abstract VisibilitySegmentType getVisibilitySegmentType(VisibilitySegmentDto visibilitySegmentDto);

    public abstract VisibilityTracksType getVisibilityTrackType(VisibilityTracksDto visibilityTracksType);

    @Mappings({
            @Mapping(source = "positions", target = "position"),
            @Mapping(source = "segments", target = "segment")
    })
    public abstract StyleSettingsType getStyleSettingsType(StyleSettingsDto styleSettingsDto);

    @Mappings({
            @Mapping(source = "position", target = "positions"),
            @Mapping(source = "segment", target = "segments")
    })
    public abstract StyleSettingsDto getStyleSettingsDto(StyleSettingsType styleSettingsType);

    @Mappings({
            @Mapping(target = "styles", expression = "java(convertToStyleType(positionsDto.getStyle()))")
    })
    public abstract PositionType getPositionType(PositionsDto positionsDto);

    @Mappings({
            @Mapping(target = "style", expression = "java(convertToStyleMap(positionType.getStyles()))")
    })
    public abstract PositionsDto getPositionsDto(PositionType positionType);

    @Mappings({
            @Mapping(target = "styles", expression = "java(convertToStyleType(segmentsDto.getStyle()))")
    })
    public abstract SegmentType getSegmentType(SegmentsDto segmentsDto);

    @Mappings({
            @Mapping(target = "style", expression = "java(convertToStyleMap(segmentType.getStyles()))")
    })
    public abstract SegmentsDto getSegmentDto(SegmentType segmentType);

    protected Boolean getAttributeValue(Boolean isAttrVisible) {
        if (isAttrVisible == null) {
            return false;
        }
        return isAttrVisible;
    }

    protected List<StyleDataType> convertToStyleType(Map<String, String> style) {
        if (style == null) {
            return null;
        }
        List<StyleDataType> styleDataTypes = new ArrayList<>();
        for (Map.Entry<String, String> entry : style.entrySet()) {
            StyleDataType styleDataType = new StyleDataType(entry.getKey(), entry.getValue());
            styleDataTypes.add(styleDataType);
        }
        return styleDataTypes;
    }

    protected Map<String, String> convertToStyleMap(List<StyleDataType> styleDataTypes) {
        if(styleDataTypes == null || styleDataTypes.isEmpty()) {
            return null;
        }
        Map<String, String> styleMap = new HashMap<>();
        for (StyleDataType styleDataType : styleDataTypes) {
            styleMap.put(styleDataType.getKey(), styleDataType.getValue());
        }
        return styleMap;
    }

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
