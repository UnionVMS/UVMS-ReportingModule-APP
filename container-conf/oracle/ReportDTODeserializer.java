/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.LayerSettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReferenceDataPropertiesDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.StyleSettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportDTODeserializer extends JsonDeserializer<ReportDTO> {

    @Override
    public ReportDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        List<FilterDTO> filterDTOList = new ArrayList<>();

        JsonNode reportIdNode = node.get(ReportDTO.ID);
        Long reportId = null;
        if (reportIdNode != null) {
            reportId = reportIdNode.getLongValue();
        }

		log.info("1. deserializer "+ReportDTO.FILTER_EXPRESSION);
        JsonNode filterNode = node.get(ReportDTO.FILTER_EXPRESSION);
		log.info("2. deserializer "+filterNode);

        if (filterNode != null) {
            addVmsFilters(filterNode.get("vms"), filterDTOList, reportId);
            addAssets(filterNode.get(AssetFilterDTO.ASSETS), filterDTOList, reportId);
            addArea(filterNode.get("areas"), filterDTOList, reportId);
            addCommon(filterNode.get("common"), filterDTOList, reportId);
        }

        boolean withMap = node.get(ReportDTO.WITH_MAP).getBooleanValue();
        return ReportDTO.ReportDTOBuilder()
                .description(node.get(ReportDTO.DESC) != null ? node.get(ReportDTO.DESC).getTextValue() : null)
                .id(node.get(ReportDTO.ID) != null ? node.get(ReportDTO.ID).getLongValue() : null)
                .name(node.get(ReportDTO.NAME).getTextValue())
                .withMap(withMap)
                .createdBy(node.get(ReportDTO.CREATED_BY) != null ? node.get(ReportDTO.CREATED_BY).getTextValue() : null)
                .filters(filterDTOList)
                .visibility(VisibilityEnum.valueOf(node.get(ReportDTO.VISIBILITY).getTextValue().toUpperCase()))
                .mapConfiguration(createMapConfigurationDTO(withMap, node.get(ReportDTO.MAP_CONFIGURATION)))
                .build();
    }

    private MapConfigurationDTO createMapConfigurationDTO(boolean withMap, JsonNode mapConfigJsonNode) throws IOException {
        if (withMap) {
            if (mapConfigJsonNode == null) {
                return MapConfigurationDTO.MapConfigurationDTOBuilder().build();
            } else {
                Long spatialConnectId = (mapConfigJsonNode.get("spatialConnectId") != null) ? mapConfigJsonNode.get("spatialConnectId").getLongValue() : null;
                Long mapProjectionId = (mapConfigJsonNode.get("mapProjectionId") != null) ? mapConfigJsonNode.get("mapProjectionId").getLongValue() : null;
                Long displayProjectionId = (mapConfigJsonNode.get("displayProjectionId") != null) ? mapConfigJsonNode.get("displayProjectionId").getLongValue() : null;
                String coordinatesFormat = (mapConfigJsonNode.get("coordinatesFormat") != null) ? mapConfigJsonNode.get("coordinatesFormat").getTextValue() : null;
                String scaleBarUnits = (mapConfigJsonNode.get("scaleBarUnits") != null) ? mapConfigJsonNode.get("scaleBarUnits").getTextValue() : null;

                ObjectMapper objectMapper = new ObjectMapper();
                VisibilitySettingsDto visibilitySettingsDto;
                StyleSettingsDto styleSettingsDto;
                LayerSettingsDto layerSettingsDto;
                Map<String, ReferenceDataPropertiesDto> referenceData;

                if (mapConfigJsonNode.get("visibilitySettings") != null) {
                    try {
                    	
                        visibilitySettingsDto = objectMapper.treeToValue(mapConfigJsonNode.get("visibilitySettings"), VisibilitySettingsDto.class);
                        
                    } catch (JsonProcessingException e) {
                        log.warn("Unable to deserialize visibilitySettings JSON property", e);
                        visibilitySettingsDto = null;
                    }
                } else {
                    visibilitySettingsDto = null;
                }

                if (mapConfigJsonNode.get("stylesSettings") != null) {
                    try {
                        styleSettingsDto = objectMapper.treeToValue(mapConfigJsonNode.get("stylesSettings"), StyleSettingsDto.class);
                    } catch (JsonProcessingException e) {
                        log.warn("Unable to deserialize stylesSettings JSON property", e);
                        styleSettingsDto = null;
                    }
                } else {
                    styleSettingsDto = null;
                }

                if (mapConfigJsonNode.get("layerSettings") != null) {
                    try {
                        layerSettingsDto = objectMapper.treeToValue(mapConfigJsonNode.get("layerSettings"), LayerSettingsDto.class);
                    } catch (JsonProcessingException e) {
                        log.warn("Unable to deserialize layerSettings JSON property", e);
                        layerSettingsDto = null;
                    }
                } else {
                    layerSettingsDto = null;
                }

                if (mapConfigJsonNode.get("referenceDataSettings") != null) {
                    try {
                        Object obj = objectMapper.treeToValue(mapConfigJsonNode.get("referenceDataSettings"), Map.class);
                        String jsonString = objectMapper.writeValueAsString(obj);
                        referenceData =objectMapper.readValue(jsonString, TypeFactory.defaultInstance().constructMapType(Map.class, String.class, ReferenceDataPropertiesDto.class));
                    } catch (IOException e) {
                        log.warn("Unable to deserialize referenceDataSettings JSON property", e);
                        referenceData = null;
                    }
                } else {
                    referenceData = null;
                }

                return MapConfigurationDTO.MapConfigurationDTOBuilder()
                        .spatialConnectId(spatialConnectId)
                        .mapProjectionId(mapProjectionId)
                        .displayProjectionId(displayProjectionId)
                        .coordinatesFormat(coordinatesFormat)
                        .scaleBarUnits(scaleBarUnits)
                        .styleSettings(styleSettingsDto)
                        .visibilitySettings(visibilitySettingsDto)
                        .layerSettings(layerSettingsDto)
                        .referenceData(referenceData)
                        .build();
            }
        } else {
            return null;
        }
    }

    private void addCommon(JsonNode common, List<FilterDTO> filterDTOList, Long reportId) throws InvalidParameterException {
        if (common != null) {

            String selectorNode = common.get("positionSelector").getValueAsText();
            Selector positionSelector = Selector.valueOf(selectorNode);

            switch (positionSelector) {
                case all:
                    handleAll(common, filterDTOList, reportId, positionSelector);
                    break;
                case last:
                    handleLast(common, filterDTOList, reportId, selectorNode, positionSelector);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleLast(JsonNode common, List<FilterDTO> filterDTOList, Long reportId, String selectorNode, Selector positionSelector) {

        String startDateLast = null;
        String endDateLast = null;

        if (common.get("startDate") != null) {
            startDateLast = common.get("startDate").getValueAsText();
        }
        if (common.get("endDate") != null) {
            endDateLast = common.get("endDate").getValueAsText();
        }
        Double value = common.get(PositionSelectorDTO.X_VALUE).getDoubleValue();

        CommonFilterDTO dto = CommonFilterDTO.CommonFilterDTOBuilder()
                .id(common.get(FilterDTO.ID) != null ? common.get(FilterDTO.ID).getLongValue() : null)
                .reportId(reportId)
                .startDate(startDateLast != null ? UI_FORMATTER.parseDateTime(startDateLast).toDate() : null)
                .endDate(endDateLast != null ? UI_FORMATTER.parseDateTime(endDateLast).toDate() : null)
                .build();
        filterDTOList.add(dto);

        JsonNode selectorType = common.get(PositionSelectorDTO.POSITION_TYPE_SELECTOR);

        if (selectorNode != null) {
            dto.setPositionSelector(PositionSelectorDTO.PositionSelectorDTOBuilder()
                    .value(value.floatValue())
                    .position(Position.valueOf(selectorType.getTextValue()))
                    .selector(positionSelector).build());

        }
    }

    private void handleAll(JsonNode common, List<FilterDTO> filterDTOList, Long reportId, Selector positionSelector) {
        JsonNode startDateNode = common.get("startDate");
        JsonNode endDateNode = common.get("endDate");

        if (startDateNode == null) {
            throw new InvalidParameterException("StartDate is mandatory when selecting ALL");
        }
        if (endDateNode == null) {
            throw new InvalidParameterException("EndDate is mandatory when selecting ALL");
        }

        String startDate = startDateNode.getValueAsText();
        String endDate = endDateNode.getValueAsText();

        filterDTOList.add(
                CommonFilterDTO.CommonFilterDTOBuilder()
                        .id(common.get(FilterDTO.ID) != null ? common.get(FilterDTO.ID).getLongValue() : null)
                        .reportId(reportId)
                        .endDate(UI_FORMATTER.parseDateTime(endDate).toDate())
                        .startDate(UI_FORMATTER.parseDateTime(startDate).toDate())
                        .positionSelector(
                                PositionSelectorDTO.PositionSelectorDTOBuilder().selector(positionSelector).build()
                        )
                        .build()
        );

    }

    private void addArea(JsonNode area, List<FilterDTO> filterDTOList, Long reportId) {
        if (area != null) {
            Iterator<JsonNode> elements = area.getElements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                filterDTOList.add(
                        AreaFilterDTO.AreaFilterDTOBuilder()
                                .reportId(reportId)
                                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).getLongValue() : null)
                                .areaId(next.get(AreaFilterDTO.JSON_ATTR_AREA_ID).getLongValue())
                                .areaType(next.get(AreaFilterDTO.JSON_ATTR_AREA_TYPE).getTextValue())
                                .build()
                );
            }
        }
    }

    private void addAssets(JsonNode asset, List<FilterDTO> filterDTOList, Long reportId) {
        if (asset != null) {
            Iterator<JsonNode> elements = asset.getElements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                FilterType type = FilterType.valueOf(next.get("type").getTextValue());
                switch (type) {
                    case asset:
                        addAssetFilterDTO(filterDTOList, reportId, next);
                        break;
                    case vgroup:
                        addAssetGroupFilterDTO(filterDTOList, reportId, next);
                        break;
                    default:
                        throw new InvalidParameterException("Unsupported parameter value");

                }
            }
        }
    }

    private void addAssetGroupFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        filterDTOList.add(
                AssetGroupFilterDTO.AssetGroupFilterDTOBuilder()
                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).getLongValue() : null)
                        .reportId(reportId)
                        .guid(next.get(AssetGroupFilterDTO.GUID).getTextValue())
                        .userName(next.get(AssetGroupFilterDTO.USER).getTextValue())
                        .name(next.get(AssetGroupFilterDTO.NAME).getTextValue())
                        .build()
        );
    }

    private void addAssetFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        filterDTOList.add(
                AssetFilterDTO.AssetFilterDTOBuilder()
                        .reportId(reportId)
                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).getLongValue() : null)
                        .guid(next.get(AssetFilterDTO.GUID).getTextValue())
                        .name(next.get(AssetFilterDTO.NAME).getTextValue())
                        .build()
        );
    }

    private void addVmsFilters(JsonNode vms, List<FilterDTO> filterDTOList, Long reportId) {
        if (vms != null) {
            Iterator<JsonNode> elements = vms.getElements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                FilterType type = FilterType.valueOf(next.get("type").getTextValue());
                switch (type) {
                    case vmspos:
                        addVmsPostisionFilterDTO(filterDTOList, reportId, next);
                        break;
                    case vmstrack:
                        addTrackFilterDTO(filterDTOList, reportId, next);
                        break;
                    case vmsseg:
                        addVmsSegmentFilterDTO(filterDTOList, reportId, next);
                        break;
                    default:
                        throw new InvalidParameterException("Unsupported parameter");

                }
            }
        }
    }

    private void addVmsPostisionFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        VmsPositionFilterDTO dto = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder()
                .reportId(reportId)
                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).getLongValue() : null)
                .maximumSpeed(next.get(VmsPositionFilterDTO.MOV_MAX_SPEED) != null ? (float)next.get(VmsPositionFilterDTO.MOV_MAX_SPEED).getDoubleValue() : null)
                .minimumSpeed(next.get(VmsPositionFilterDTO.MOV_MIN_SPEED) != null ? (float)next.get(VmsPositionFilterDTO.MOV_MIN_SPEED).getDoubleValue() : null)
                .build();

        if (next.get(VmsPositionFilterDTO.MOV_ACTIVITY) != null) {
            dto.setMovementActivity(MovementActivityTypeType
                    .valueOf(next.get(VmsPositionFilterDTO.MOV_ACTIVITY).getTextValue()));
        }
        if (next.get(VmsPositionFilterDTO.MOV_TYPE) != null) {
            dto.setMovementType(MovementTypeType
                    .valueOf(next.get(VmsPositionFilterDTO.MOV_TYPE).getTextValue()));
        }
        if (next.get(VmsPositionFilterDTO.MOV_SOURCES) != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<String> movSources = mapper.convertValue(next.get(VmsPositionFilterDTO.MOV_SOURCES), List.class);
            dto.setMovementSources(movSources);
        }
        filterDTOList.add(dto);
    }

    private void addVmsSegmentFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        VmsSegmentFilterDTO segmentFilterDTO = VmsSegmentFilterDTO.builder()
                .reportId(reportId)
                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).getLongValue() : null)
                .minimumSpeed(next.get(VmsSegmentFilterDTO.SEG_MIN_SPEED) != null ? (float)next.get(VmsSegmentFilterDTO.SEG_MIN_SPEED).getDoubleValue() : null)
                .maxDuration(next.get(VmsSegmentFilterDTO.SEG_MAX_DURATION) != null ?(float) next.get(VmsSegmentFilterDTO.SEG_MAX_DURATION).getDoubleValue() : null)
                .maximumSpeed(next.get(VmsSegmentFilterDTO.SEG_MAX_SPEED) != null ? (float)next.get(VmsSegmentFilterDTO.SEG_MAX_SPEED).getDoubleValue() : null)
                .minDuration(next.get(VmsSegmentFilterDTO.SEG_MIN_DURATION) != null ? (float)next.get(VmsSegmentFilterDTO.SEG_MIN_DURATION).getDoubleValue() : null)
                .build();

        if (next.get(VmsSegmentFilterDTO.SEG_CATEGORY) != null) {
            segmentFilterDTO.setCategory(SegmentCategoryType
                    .valueOf(next.get(VmsSegmentFilterDTO.SEG_CATEGORY).getTextValue()));
        }

        filterDTOList.add(segmentFilterDTO);
    }

    private void addTrackFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        filterDTOList.add(
                TrackFilterDTO.builder()
                        .reportId(reportId)
                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).getLongValue() : null)
                        .minDuration(next.get(TrackFilterDTO.TRK_MIN_DURATION) != null ? (float)next.get(TrackFilterDTO.TRK_MIN_DURATION).getDoubleValue() : null)
                        .maxTime(next.get(TrackFilterDTO.TRK_MAX_TIME) != null ? (float)next.get(TrackFilterDTO.TRK_MAX_TIME).getDoubleValue() : null)
                        .maxDuration(next.get(TrackFilterDTO.TRK_MAX_DURATION) != null ? (float)next.get(TrackFilterDTO.TRK_MAX_DURATION).getDoubleValue() : null)
                        .minTime(next.get(TrackFilterDTO.TRK_MIN_TIME) != null ? (float)next.get(TrackFilterDTO.TRK_MIN_TIME).getDoubleValue() : null)
                        .build()
        );
    }
}