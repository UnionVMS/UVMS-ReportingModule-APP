/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.util;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.ASSETS;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.CREATED_BY;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.CREATED_ON;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.DESC;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.END_DATE;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.FILTER_EXPRESSION;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.GUID;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.ID;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.MAP_CONFIGURATION;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.NAME;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.REPORT_TYPE;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.START_DATE;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.VISIBILITY;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.WITH_MAP;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CriteriaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FaWeight;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.LayerSettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReferenceDataPropertiesDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.StyleSettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.GroupCriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.GroupCriteriaFilterMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.IteratorUtils;

@Slf4j
public class ReportDeserializer extends JsonDeserializer<ReportDTO> {

    @Override
    public ReportDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        ReportTypeEnum reportTypeEnum =
                node.get(REPORT_TYPE) != null ? ReportTypeEnum.getReportTypeEnum(node.get(REPORT_TYPE).textValue()) : ReportTypeEnum.STANDARD;

        List<FilterDTO> filterDTOList = new ArrayList<>();

        JsonNode reportIdNode = node.get(ID);
        Long reportId = null;
        if (reportIdNode != null) {
            reportId = reportIdNode.longValue();
        }

        JsonNode filterNode = node.get(FILTER_EXPRESSION);

        if (filterNode != null) {
            addVmsFilters(filterNode.get("vms"), filterDTOList, reportId);
            addAssets(filterNode.get(ASSETS), filterDTOList, reportId);
            addArea(filterNode.get("areas"), filterDTOList, reportId);
            addCommon(filterNode.get("common"), filterDTOList, reportId);
            addFaFilters(filterNode.get("fa"), filterDTOList, reportId);
            if (ReportTypeEnum.SUMMARY.equals(reportTypeEnum)){
                addGroupCriteria(filterNode.get("criteria"), filterDTOList, reportId, jsonParser);
            }
        }

        boolean withMap = false;
        JsonNode witMapNode = node.get(WITH_MAP);
        if (witMapNode != null) {
            withMap = witMapNode.booleanValue();
        }

        VisibilityEnum visibilityEnum = null;
        JsonNode visibilityNode = node.get(VISIBILITY);
        if (visibilityNode != null) {
            String s = visibilityNode.textValue();
            if (s != null) {
                visibilityEnum = VisibilityEnum.valueOf(s.toUpperCase());
            }
        }

        String nameValue = null;
        JsonNode nameNode = node.get(NAME);
        if (nameNode != null) {
            nameValue = nameNode.textValue();
        }

        JsonNode createdOnNode = node.get(CREATED_ON);
        String createdOnValue = null;
        if (createdOnNode != null) {
            createdOnValue = createdOnNode.textValue();
        }

        JsonNode editableNode = node.get("editable");
        boolean editableValue = false;
        if (createdOnNode != null) {
            editableValue = editableNode.booleanValue();
        }


        ReportDTO build = ReportDTO.builder()
                .description(node.get(DESC) != null ? node.get(DESC).textValue() : null)
                .id(node.get(ID) != null ? node.get(ID).longValue() : null)
                .name(nameValue)
                .withMap(withMap)
                .createdBy(node.get(CREATED_BY) != null ? node.get(CREATED_BY).textValue() : null)
                .filters(filterDTOList)
                .visibility(visibilityEnum)
                .mapConfiguration(createMapConfigurationDTO(withMap, node.get(MAP_CONFIGURATION)))
                .build();
        build.setReportTypeEnum(reportTypeEnum);
        build.setAudit(new AuditDTO(createdOnValue));
        build.setEditable(editableValue);
        return build;
    }

    private MapConfigurationDTO createMapConfigurationDTO(boolean withMap, JsonNode mapConfigJsonNode) {
        if (withMap) {
            if (mapConfigJsonNode == null) {
                return MapConfigurationDTO.MapConfigurationDTOBuilder().build();
            } else {
                Long spatialConnectId = (mapConfigJsonNode.get("spatialConnectId") != null) ? mapConfigJsonNode.get("spatialConnectId").longValue() : null;
                Long mapProjectionId = (mapConfigJsonNode.get("mapProjectionId") != null) ? mapConfigJsonNode.get("mapProjectionId").longValue() : null;
                Long displayProjectionId = (mapConfigJsonNode.get("displayProjectionId") != null) ? mapConfigJsonNode.get("displayProjectionId").longValue() : null;
                String coordinatesFormat = (mapConfigJsonNode.get("coordinatesFormat") != null) ? mapConfigJsonNode.get("coordinatesFormat").textValue() : null;
                String scaleBarUnits = (mapConfigJsonNode.get("scaleBarUnits") != null) ? mapConfigJsonNode.get("scaleBarUnits").textValue() : null;

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

    private void addGroupCriteria(JsonNode groupBy, List<FilterDTO> filterDTOList, Long reportId, JsonParser jp) {

        if (groupBy != null){
            List list = IteratorUtils.toList(groupBy.elements());
            for (int i = 0; i < list.size(); i++){
                String code = ((JsonNode)list.get(i)).get("code").asText();
                JsonNode valueNode = ((JsonNode) list.get(i)).get("values");
                List<GroupCriteriaType> groupCriteriaList = null;
                if(valueNode != null){
                    List<String> strings = ((ObjectMapper) jp.getCodec()).convertValue(valueNode, List.class);
                    groupCriteriaList = GroupCriteriaFilterMapper.INSTANCE.mapGroupCriteriaTypeListToStringList(strings);
                }
                filterDTOList.add(new CriteriaFilterDTO(code, groupCriteriaList, i + 1, reportId));
            }
        }
    }

    private void addFaFilters(JsonNode fa, List<FilterDTO> filterDTOList, Long reportId) throws JsonProcessingException {
        if (fa != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            FaFilter faFilter = objectMapper.treeToValue(fa, FaFilter.class);
            FaFilterDTO faFilterDTO = FaFilterDTO.FaFilterBuilder().
                    id(fa.get(ID) != null ? fa.get(ID).longValue() : null)
                    .reportId(reportId)
                    .reportTypes(faFilter.getReportTypes())
                    .activityTypes(faFilter.getActivityTypes())
                    .masters(faFilter.getMasters())
                    .ports(faFilter.getFaPorts())
                    .gears(faFilter.getFaGears())
                    .species(faFilter.getSpecies())
                    .faWeight(faFilter.getFaWeight() != null ? new FaWeight(faFilter.getFaWeight().getMin(), faFilter.getFaWeight().getMax(), faFilter.getFaWeight().getUnit()) : null)
                    .build();
            filterDTOList.add(faFilterDTO);
        }

    }

    private void addCommon(JsonNode common, List<FilterDTO> filterDTOList, Long reportId) {
        if (common != null) {

            JsonNode selectorNode = common.get("positionSelector");
            String selectorNodeValue;
            if (selectorNode != null) {
                selectorNodeValue = selectorNode.asText();
                Selector positionSelector = Selector.valueOf(selectorNodeValue);

                switch (positionSelector) {
                    case all:
                        handleAll(common, filterDTOList, reportId, positionSelector);
                        break;
                    case last:
                        handleLast(common, filterDTOList, reportId, selectorNodeValue, positionSelector);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void handleLast(JsonNode common, List<FilterDTO> filterDTOList, Long reportId, String selectorNode, Selector positionSelector) {

        String startDateLast = null;
        String endDateLast = null;

        if (common.get(START_DATE) != null) {
            startDateLast = common.get(START_DATE).asText();
        }
        if (common.get(END_DATE) != null) {
            endDateLast = common.get(END_DATE).asText();
        }
        Float value = common.get(PositionSelectorDTO.X_VALUE).floatValue();

        CommonFilterDTO dto = CommonFilterDTO.CommonFilterDTOBuilder()
                .id(common.get(FilterDTO.ID) != null ? common.get(FilterDTO.ID).longValue() : null)
                .reportId(reportId)
                .startDate(startDateLast != null ? UI_FORMATTER.parseDateTime(startDateLast).toDate() : null)
                .endDate(endDateLast != null ? UI_FORMATTER.parseDateTime(endDateLast).toDate() : null)
                .build();
        filterDTOList.add(dto);

        JsonNode selectorType = common.get(PositionSelectorDTO.POSITION_TYPE_SELECTOR);

        if (selectorNode != null) {
            dto.setPositionSelector(PositionSelectorDTO.PositionSelectorDTOBuilder()
                    .value(value)
                    .position(Position.valueOf(selectorType.textValue()))
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

        String startDate = startDateNode.asText();
        String endDate = endDateNode.asText();

        filterDTOList.add(
                CommonFilterDTO.CommonFilterDTOBuilder()
                        .id(common.get(FilterDTO.ID) != null ? common.get(FilterDTO.ID).longValue() : null)
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
            Iterator<JsonNode> elements = area.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                filterDTOList.add(
                        AreaFilterDTO.AreaFilterDTOBuilder()
                                .reportId(reportId)
                                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                .areaId(next.get(AreaFilterDTO.JSON_ATTR_AREA_ID).longValue())
                                .areaType(next.get(AreaFilterDTO.JSON_ATTR_AREA_TYPE).textValue())
                                .build()
                );
            }
        }
    }

    private void addAssets(JsonNode asset, List<FilterDTO> filterDTOList, Long reportId) {
        if (asset != null) {
            Iterator<JsonNode> elements = asset.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                FilterType type = FilterType.valueOf(next.get("type").textValue());
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
                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                        .reportId(reportId)
                        .guid(next.get(AssetGroupFilterDTO.GUID).textValue())
                        .userName(next.get(AssetGroupFilterDTO.USER).textValue())
                        .name(next.get(AssetGroupFilterDTO.NAME).textValue())
                        .build()
        );
    }

    private void addAssetFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        AssetFilterDTO asset = new AssetFilterDTO();
        asset.setReportId(reportId);

        JsonNode idNode = next.get(FilterDTO.ID);
        if (idNode != null) {
            long id = idNode.longValue();
            asset.setId(id);
        }

        JsonNode guidNode = next.get(GUID);
        if (guidNode != null) {
            String guidValue = guidNode.textValue();
            asset.setGuid(guidValue);
        }

        JsonNode nameNode = next.get(NAME);
        if (nameNode != null) {
            String nameValue = nameNode.textValue();
            asset.setName(nameValue);
        }

        filterDTOList.add(asset);
    }

    private void addVmsFilters(JsonNode vms, List<FilterDTO> filterDTOList, Long reportId) {
        if (vms != null) {
            Iterator<JsonNode> elements = vms.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                FilterType type = FilterType.valueOf(next.get("type").textValue());
                switch (type) {
                    case vmspos:
                        addPositionFilter(filterDTOList, reportId, next);
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

    private void addPositionFilter(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        VmsPositionFilterDTO dto = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder()
                .reportId(reportId)
                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                .maximumSpeed(next.get(VmsPositionFilterDTO.MOV_MAX_SPEED) != null ? next.get(VmsPositionFilterDTO.MOV_MAX_SPEED).floatValue() : null)
                .minimumSpeed(next.get(VmsPositionFilterDTO.MOV_MIN_SPEED) != null ? next.get(VmsPositionFilterDTO.MOV_MIN_SPEED).floatValue() : null)
                .build();

        if (next.get(VmsPositionFilterDTO.MOV_ACTIVITY) != null) {
            dto.setMovementActivity(MovementActivityTypeType
                    .valueOf(next.get(VmsPositionFilterDTO.MOV_ACTIVITY).textValue()));
        }
        if (next.get(VmsPositionFilterDTO.MOV_TYPE) != null) {
            dto.setMovementType(MovementTypeType
                    .valueOf(next.get(VmsPositionFilterDTO.MOV_TYPE).textValue()));
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
                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                .minimumSpeed(next.get(VmsSegmentFilterDTO.SEG_MIN_SPEED) != null ? next.get(VmsSegmentFilterDTO.SEG_MIN_SPEED).floatValue() : null)
                .maxDuration(next.get(VmsSegmentFilterDTO.SEG_MAX_DURATION) != null ? next.get(VmsSegmentFilterDTO.SEG_MAX_DURATION).floatValue() : null)
                .maximumSpeed(next.get(VmsSegmentFilterDTO.SEG_MAX_SPEED) != null ? next.get(VmsSegmentFilterDTO.SEG_MAX_SPEED).floatValue() : null)
                .minDuration(next.get(VmsSegmentFilterDTO.SEG_MIN_DURATION) != null ? next.get(VmsSegmentFilterDTO.SEG_MIN_DURATION).floatValue() : null)
                .build();

        if (next.get(VmsSegmentFilterDTO.SEG_CATEGORY) != null) {
            segmentFilterDTO.setCategory(SegmentCategoryType
                    .valueOf(next.get(VmsSegmentFilterDTO.SEG_CATEGORY).textValue()));
        }

        filterDTOList.add(segmentFilterDTO);
    }

    private void addTrackFilterDTO(List<FilterDTO> filterDTOList, Long reportId, JsonNode next) {
        TrackFilterDTO track = TrackFilterDTO.builder()
                .reportId(reportId)
                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                .minDuration(next.get(TrackFilterDTO.TRK_MIN_DURATION) != null ? next.get(TrackFilterDTO.TRK_MIN_DURATION).floatValue() : null)
                .maxTime(next.get(TrackFilterDTO.TRK_MAX_TIME) != null ? next.get(TrackFilterDTO.TRK_MAX_TIME).floatValue() : null)
                .maxDuration(next.get(TrackFilterDTO.TRK_MAX_DURATION) != null ? next.get(TrackFilterDTO.TRK_MAX_DURATION).floatValue() : null)
                .minTime(next.get(TrackFilterDTO.TRK_MIN_TIME) != null ? next.get(TrackFilterDTO.TRK_MIN_TIME).floatValue() : null)
                .build();

        JsonNode minDistanceNode = next.get("minDistance");
        if (minDistanceNode != null) {
            track.setMinDistance(minDistanceNode.floatValue());
        }

        JsonNode maxDistanceNode = next.get("maxDistance");
        if (maxDistanceNode != null) {
            track.setMaxDistance(maxDistanceNode.floatValue());
        }

        JsonNode minAvgSpeedNode = next.get("minAvgSpeed");
        if (minAvgSpeedNode != null) {
            track.setMinAvgSpeed(minAvgSpeedNode.floatValue());
        }

        JsonNode maxAvgSpeedNode = next.get("maxAvgSpeed");
        if (minAvgSpeedNode != null) {
            track.setMaxAvgSpeed(maxAvgSpeedNode.floatValue());
        }
        filterDTOList.add(track);

    }
}