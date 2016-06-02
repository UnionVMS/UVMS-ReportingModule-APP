package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.util.AreaTypeEnum;
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
            @Mapping(target = "styleSettings", ignore = true),
            @Mapping(target = "layerSettings", ignore = true),
            @Mapping(target = "referenceData", ignore = true)
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

    public abstract LayerSettingsType getLayerSettingsType(LayerSettingsDto layerSettingsDto);

    @Mappings({
            @Mapping(target = "baseLayers", expression = "java(getLayers(layerSettingsType.getBaseLayers()))"),
            @Mapping(target = "portLayers", expression = "java(getLayers(layerSettingsType.getPortLayers()))"),
            @Mapping(target = "additionalLayers", expression = "java(getLayers(layerSettingsType.getAdditionalLayers()))"),
            @Mapping(target = "areaLayers", expression = "java(getAreaLayers(layerSettingsType.getAreaLayers()))")
    })
    public abstract LayerSettingsDto getLayerSettingsDto(LayerSettingsType layerSettingsType);

    public List<ReferenceDataType> getReferenceDataType(Map<String, ReferenceDataPropertiesDto> referenceData) {
        if (referenceData == null || referenceData.isEmpty()) {
            return null;
        }
        List<ReferenceDataType> referenceDataTypes = new ArrayList<>();
        for (Map.Entry<String, ReferenceDataPropertiesDto> entry : referenceData.entrySet()) {
            ReferenceDataPropertiesDto referenceDataPropertiesDto = entry.getValue();
            referenceDataTypes.add(new ReferenceDataType(entry.getKey(), referenceDataPropertiesDto.getSelection(), referenceDataPropertiesDto.getCodes()));
        }
        return referenceDataTypes;
    }

    public Map<String, ReferenceDataPropertiesDto> getReferenceData(List<ReferenceDataType> referenceDataTypes) {
        if (referenceDataTypes == null || referenceDataTypes.isEmpty()) {
            return null;
        }
        Map<String, ReferenceDataPropertiesDto> referenceData = new HashMap<>();
        for (ReferenceDataType referenceDataType : referenceDataTypes) {
            referenceData.put(referenceDataType.getType(),
                    new ReferenceDataPropertiesDto(referenceDataType.getSelection(), referenceDataType.getCodes()));
        }
        return referenceData;
    }

    protected List<LayersDto> getLayers(List<LayersType> layersTypes) {
        if (layersTypes == null || layersTypes.isEmpty()) {
            return null;
        }
        List<LayersDto> layersDtos = new ArrayList<>();
        for (LayersType layersType : layersTypes) {
            layersDtos.add(getLayersDto(layersType));
        }
        return layersDtos;
    }

    protected List<LayerAreaDto> getAreaLayers(List<LayerAreaType> layerAreaTypes) {
        if (layerAreaTypes == null || layerAreaTypes.isEmpty()) {
            return null;
        }
        List<LayerAreaDto> layerAreaDtos = new ArrayList<>();
        for (LayerAreaType layerAreaType : layerAreaTypes) {
            layerAreaDtos.add(getLayerAreaDto(layerAreaType));
        }
        return layerAreaDtos;
    }

    public abstract LayersType getLayersType(LayersDto layersDto);

    @Mappings({
            @Mapping(target = "order", expression = "java(getOrder(layersType))")
    })
    public abstract LayersDto getLayersDto(LayersType layersType);

    protected Long getOrder(LayersType layersType) {
        if (layersType.getOrder() == 0) {
            return null;
        }
        return layersType.getOrder();
    }

    protected Long getGid(LayerAreaType layersType) {
        if (layersType.getGid() == 0) {
            return null;
        }
        return layersType.getGid();
    }

    @Mappings({
            @Mapping(target = "areaType", expression = "java(getAreaType(layerAreaDto.getAreaType()))")
    })
    public abstract LayerAreaType getLayerAreaType(LayerAreaDto layerAreaDto);

    @Mappings({
            @Mapping(target = "areaType", expression = "java(getAreaTypeEnum(layerAreaType.getAreaType()))"),
            @Mapping(target = "order", expression = "java(getOrder(layerAreaType))"),
            @Mapping(target = "gid", expression = "java(getGid(layerAreaType))")
    })
    public abstract LayerAreaDto getLayerAreaDto(LayerAreaType layerAreaType);

    @Mappings({
            @Mapping(source = "positions", target = "position"),
            @Mapping(source = "segments", target = "segment"),
            @Mapping(source = "alarms", target = "alarm")
    })
    public abstract StyleSettingsType getStyleSettingsType(StyleSettingsDto styleSettingsDto);

    @Mappings({
            @Mapping(source = "position", target = "positions"),
            @Mapping(source = "segment", target = "segments"),
            @Mapping(source = "alarm", target = "alarms")
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

    protected String getAreaType(AreaTypeEnum areaTypeEnum) {
        return areaTypeEnum.getType();
    }

    protected AreaTypeEnum getAreaTypeEnum(String areaType) {
        return AreaTypeEnum.valueOf(areaType);
    }

    protected AlarmType getAlarmType(AlarmsDto alarmsDto) {
        if (alarmsDto == null) {
            return null;
        }
        AlarmType alarmType = new AlarmType();
        alarmType.setSize(alarmsDto.getSize());
        alarmType.setOpen(alarmsDto.getOpen());
        alarmType.setClosed(alarmsDto.getClosed());
        alarmType.setPending(alarmsDto.getPending());
        alarmType.setNone(alarmsDto.getNone());
        return alarmType;
    }

    protected AlarmsDto getAlarmDto(AlarmType alarmType) {
        if (alarmType == null) {
            return null;
        }
        AlarmsDto alarmsDto = new AlarmsDto();
        alarmsDto.setSize(alarmType.getSize());
        alarmsDto.setOpen(alarmType.getOpen());
        alarmsDto.setClosed(alarmType.getClosed());
        alarmsDto.setPending(alarmType.getPending());
        alarmsDto.setNone(alarmType.getNone());
        return alarmsDto;
    }

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
