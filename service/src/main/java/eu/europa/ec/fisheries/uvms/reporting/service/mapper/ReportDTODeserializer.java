package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;

public class ReportDTODeserializer extends JsonDeserializer<ReportDTO> {

    @Override
    public ReportDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        List<FilterDTO> filterDTOList = new ArrayList<>();

        JsonNode reportIdNode = node.get(ReportDTO.ID);
        Long reportId = null;
        if (reportIdNode != null){
            reportId = reportIdNode.longValue();
        }

        JsonNode filterNode = node.get(ReportDTO.FILTER_EXPRESSION);

        if (filterNode != null) {
            addVmsFilters(filterNode.get("vms"), filterDTOList, reportId);
            addVessels(filterNode.get(VesselFilterDTO.VESSELS), filterDTOList, reportId);
            addArea(filterNode.get("areas"), filterDTOList, reportId);
            addCommon(filterNode.get("common"), filterDTOList, reportId);
        }

        return ReportDTO.ReportDTOBuilder()
                .description(node.get(ReportDTO.DESC) != null? node.get(ReportDTO.DESC).textValue() : null)
                .id(node.get(ReportDTO.ID) != null ? node.get(ReportDTO.ID).longValue() : null)
                .name(node.get(ReportDTO.NAME).textValue())
                .withMap(node.get(ReportDTO.WITH_MAP).booleanValue())
                .createdBy(node.get(ReportDTO.CREATED_BY)!= null ? node.get(ReportDTO.CREATED_BY).textValue() : null)
                .filters(filterDTOList)
                .visibility(VisibilityEnum.valueOf(node.get(ReportDTO.VISIBILITY).textValue().toUpperCase()))
                .build();
    }

    private void addCommon(JsonNode common, List<FilterDTO> filterDTOList, Long reportId) throws InvalidParameterException {
        if (common != null){

            String selectorNode = common.get("positionSelector").asText();
            Selector positionSelector = Selector.valueOf(selectorNode);

            switch (positionSelector){
                case all:
                    String startDate = common.get("startDate").asText();
                    String endDate = common.get("endDate").asText();
                    if (startDate == null){
                        throw new InvalidParameterException("StartDate is mandatory when selecting ALL");
                    }
                    if (endDate == null){
                        throw new InvalidParameterException("EndDate is mandatory when selecting ALL");
                    }
                    try{
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
                    catch (Exception e){
                        throw new InvalidParameterException("Invalid parameters");
                    }

                    break;
                case last:
                    String startDateLast = null;
                    String endDateLast = null;
                    try{

                        if (common.get("startDate") != null){
                            startDateLast = common.get("startDate").asText();
                        }
                        if (common.get("endDate") != null){
                            endDateLast = common.get("endDate").asText();
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
                        if(selectorNode != null){
                            dto.setPositionSelector(PositionSelectorDTO.PositionSelectorDTOBuilder()
                                    .value(value)
                                    .position(Position.valueOf(selectorType.textValue()))
                                    .selector(positionSelector).build());

                        }
                    }

                    catch (Exception e){
                        throw new InvalidParameterException("Invalid parameters");
                    }
            }
        }
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

    private void addVessels(JsonNode vessel, List<FilterDTO> filterDTOList, Long reportId) {
        if (vessel != null){
            Iterator<JsonNode> elements = vessel.elements();
            while(elements.hasNext()) {
                JsonNode next = elements.next();
                FilterType type = FilterType.valueOf(next.get("type").textValue());
                switch(type){
                    case vessel:
                        filterDTOList.add(
                                VesselFilterDTO.VesselFilterDTOBuilder()
                                        .reportId(reportId)
                                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                        .guid(next.get(VesselFilterDTO.GUID).textValue())
                                        .name(next.get(VesselFilterDTO.NAME).textValue())
                                        .build()
                        );
                        break;
                    case vgroup:
                        filterDTOList.add(
                                VesselGroupFilterDTO.VesselGroupFilterDTOBuilder()
                                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                        .reportId(reportId)
                                        .guid(next.get(VesselGroupFilterDTO.GUID).textValue())
                                        .userName(next.get(VesselGroupFilterDTO.USER).textValue())
                                        .name(next.get(VesselGroupFilterDTO.NAME).textValue())
                                        .build()
                        );
                        break;
                    default:
                        throw new InvalidParameterException("Unsupported parameter value");

                }
            }
        }
    }

    private void addVmsFilters(JsonNode vms, List<FilterDTO> filterDTOList, Long reportId) {
        if (vms != null) {
            Iterator<JsonNode> elements = vms.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                FilterType type = FilterType.valueOf(next.get("type").textValue());
                switch (type) {
                    case vmspos:
                        VmsPositionFilterDTO dto = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder()
                                .reportId(reportId)
                                .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                .maximumSpeed(next.get(VmsPositionFilterDTO.MOV_MAX_SPEED) != null ? next.get(VmsPositionFilterDTO.MOV_MAX_SPEED).floatValue() : null)
                                .minimumSpeed(next.get(VmsPositionFilterDTO.MOV_MIN_SPEED) != null ? next.get(VmsPositionFilterDTO.MOV_MIN_SPEED).floatValue() : null)
                                .build();

                        if (next.get(VmsPositionFilterDTO.MOV_ACTIVITY) != null){
                            dto.setMovementActivity(MovementActivityTypeType
                                    .valueOf(next.get(VmsPositionFilterDTO.MOV_ACTIVITY).textValue()));
                        }
                        if (next.get(VmsPositionFilterDTO.MOV_TYPE) != null){
                            dto.setMovementType(MovementTypeType
                                    .valueOf(next.get(VmsPositionFilterDTO.MOV_TYPE).textValue()));
                        }
                        filterDTOList.add(dto);

                        break;
                    case vmstrack:
                        filterDTOList.add(
                                TrackFilterDTO.TrackFilterDTOBuild()
                                        .reportId(reportId)
                                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                        .minDuration(next.get(TrackFilterDTO.TRK_MIN_DURATION) != null ? next.get(TrackFilterDTO.TRK_MIN_DURATION).floatValue() : null)
                                        .maxTime(next.get(TrackFilterDTO.TRK_MAX_TIME) != null ? next.get(TrackFilterDTO.TRK_MAX_TIME).floatValue() : null)
                                        .maxDuration(next.get(TrackFilterDTO.TRK_MAX_DURATION) != null ? next.get(TrackFilterDTO.TRK_MAX_DURATION).floatValue() : null)
                                        .minTime(next.get(TrackFilterDTO.TRK_MIN_TIME) != null ? next.get(TrackFilterDTO.TRK_MIN_TIME).floatValue() : null)
                                        .build()
                        );
                        break;
                    case vmsseg:
                        filterDTOList.add(
                                VmsSegmentFilterDTO.VmsSegmentFilterDTOBuilder()
                                        .reportId(reportId)
                                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                        .minimumSpeed(next.get(VmsSegmentFilterDTO.SEG_MIN_SPEED) != null ? next.get(VmsSegmentFilterDTO.SEG_MIN_SPEED).floatValue() : null)
                                        .maxDuration(next.get(VmsSegmentFilterDTO.SEG_MAX_DURATION) != null ? next.get(VmsSegmentFilterDTO.SEG_MAX_DURATION).floatValue() : null)
                                        .maximumSpeed(next.get(VmsSegmentFilterDTO.SEG_MAX_SPEED) != null ? next.get(VmsSegmentFilterDTO.SEG_MAX_SPEED).floatValue() : null)
                                        .minDuration(next.get(VmsSegmentFilterDTO.SEG_MIN_DURATION) != null ? next.get(VmsSegmentFilterDTO.SEG_MIN_DURATION).floatValue() : null)
                                        .category(next.get(VmsSegmentFilterDTO.SEG_CATEGORY) != null ? next.get(VmsSegmentFilterDTO.SEG_CATEGORY).asText() : null)
                                        .build()
                        );
                        break;
                    default:
                        throw new InvalidParameterException("Unsupported parameter");

                }
            }
        }
    }
}
