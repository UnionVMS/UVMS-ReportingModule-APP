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

        addVmsFilters(filterNode.get("vms"), filterDTOList, reportId);
        addVessels(filterNode.get("vessels"), filterDTOList, reportId);
        addArea(filterNode.get("area"), filterDTOList, reportId);
        addCommon(filterNode.get("common"), filterDTOList, reportId);

        return ReportDTO.ReportDTOBuilder()
                .description(node.get(ReportDTO.DESC) != null? node.get(ReportDTO.DESC).textValue() : null)
                .id(node.get(ReportDTO.ID) != null ? node.get(ReportDTO.ID).longValue() : null)
                .name(node.get(ReportDTO.NAME).textValue())
                .withMap(node.get(ReportDTO.WITH_MAP).booleanValue())
                .filters(filterDTOList)
                .visibility(VisibilityEnum.valueOf(node.get(ReportDTO.VISIBILITY).textValue().toUpperCase()))
                .build();
    }

    private void addCommon(JsonNode common, List<FilterDTO> filterDTOList, Long reportId) throws InvalidParameterException {
        if (common != null){

            String selectorNode = common.get("positionSelector").asText().toUpperCase();
            Selector positionSelector = Selector.valueOf(selectorNode);

            switch (positionSelector){
                case ALL:
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
                case LAST:
                    Float value = common.get("value").floatValue();
                    try{
                        filterDTOList.add(
                                CommonFilterDTO.CommonFilterDTOBuilder()
                                        .id(common.get(FilterDTO.ID) != null ? common.get(FilterDTO.ID).longValue() : null)
                                        .reportId(reportId)
                                        .positionSelector(
                                                PositionSelectorDTO.PositionSelectorDTOBuilder().value(value)
                                                        .selector(positionSelector).build()
                                        )
                                        .build()
                        );
                    }
                    catch (Exception e){
                        throw new InvalidParameterException("Invalid parameters");
                    }
            }
        }
    }

    private void addArea(JsonNode area, List<FilterDTO> filterDTOList, Long reportId) {
        if (area != null){
            throw new InvalidParameterException("Unimplemented functionality");
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
                        filterDTOList.add(
                                VmsPositionFilterDTO.VmsPositionFilterDTOBuilder()
                                        .reportId(reportId)
                                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                        .maximumSpeed(next.get(VmsPositionFilterDTO.MOV_MAX_SPEED) != null ? next.get(VmsPositionFilterDTO.MOV_MAX_SPEED).floatValue() : null)
                                        .minimumSpeed(next.get(VmsPositionFilterDTO.MOV_MIN_SPEED) != null ? next.get(VmsPositionFilterDTO.MOV_MIN_SPEED).floatValue() : null)
                                        .movementActivity(MovementActivityTypeType
                                                .valueOf(next.get(VmsPositionFilterDTO.MOV_ACTIVITY) != null ? next.get(VmsPositionFilterDTO.MOV_ACTIVITY).textValue() : null))
                                        .movementType(MovementTypeType
                                                .valueOf(next.get(VmsPositionFilterDTO.MOV_TYPE) != null ? next.get(VmsPositionFilterDTO.MOV_TYPE).textValue() : null))
                                        .build()
                        );
                        break;
                    case vmstrack:
                        filterDTOList.add(
                                TrackFilterDTO.TrackFilterDTOBuild()
                                        .reportId(reportId)
                                        .id(next.get(FilterDTO.ID) != null ? next.get(FilterDTO.ID).longValue() : null)
                                        .minDuration(next.get(TrackFilterDTO.TRK_MIN_DURATION) != null ? next.get(TrackFilterDTO.TRK_MIN_DURATION).floatValue() : null)
                                        .maxTime(next.get(TrackFilterDTO.TRK_MAX_TIME) != null ? next.get(TrackFilterDTO.TRK_MAX_TIME).floatValue() : null)
                                        .maxDuration(next.get(TrackFilterDTO.TRK_MAX_DURATION) != null ? next.get(TrackFilterDTO.TRK_MAX_DURATION).floatValue() : null)
                                        .maxTime(next.get(TrackFilterDTO.TRK_MAX_TIME) != null ? next.get(TrackFilterDTO.TRK_MAX_TIME).floatValue() : null)
                                        .build()
                        );
                        break;
                    default:
                        throw new InvalidParameterException("Unsupported parameter");
                        // case SEGMENT:

                }
            }
        }
    }
}
