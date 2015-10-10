package eu.europa.ec.fisheries.uvms.reporting.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.*;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector.ALL;

/**
 * //TODO create test
 */
public class ReportDTOSerializer extends com.fasterxml.jackson.databind.ser.std.StdSerializer<ReportDTO> {

    public ReportDTOSerializer(Class<ReportDTO> t) {
        super(t);
    }

    @Override
    public void serialize(ReportDTO reportDTO, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {

        Set<FilterDTO> filters = reportDTO.getFilters();

        FilterDTO position = null;
        FilterDTO segment = null;
        FilterDTO track = null;

        List<FilterDTO> vesselFilterDTOList = new ArrayList<>();
        CommonFilterDTO commonFilter = null;

        for(FilterDTO filterDTO : filters){
            FilterType type = filterDTO.getType();
            switch (type){
                case COMMON:
                    commonFilter = (CommonFilterDTO) filterDTO;
                    break;
                case VMSPOS:
                    position = filterDTO;
                    break;
                case VESSEL:
                case VGROUP:
                    vesselFilterDTOList.add(filterDTO);
                    break;
            }
        }

        jgen.writeStartObject();
        writeReport(reportDTO, jgen);
                jgen.writeStartObject();
                writeCommon(jgen, commonFilter);
                writeVessels(jgen, vesselFilterDTOList);
                writeVms(jgen, position);
                jgen.writeEndObject();
        jgen.writeEndObject();

    }

    protected void writeVessels(JsonGenerator jgen, List<FilterDTO> vesselFilterDTOList) throws IOException {
        if(CollectionUtils.isNotEmpty(vesselFilterDTOList)) {
            jgen.writeObjectField("vessels", vesselFilterDTOList);
        }
    }

    protected void writeVms(JsonGenerator jgen, FilterDTO position) throws IOException {
        jgen.writeFieldName("vms");
        if (position != null){
            jgen.writeObject(position);
        }
    }

    protected void writeReport(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        jgen.writeNumberField(ID, reportDTO.getId());
        jgen.writeStringField(NAME, reportDTO.getName());
        jgen.writeStringField(DESC, reportDTO.getDescription());
        jgen.writeStringField(VISIBILITY, reportDTO.getVisibility().name());
        jgen.writeStringField("createdOn", DateUtils.dateToString(reportDTO.getAudit().getCreatedOn()));
        jgen.writeStringField(CREATED_BY, reportDTO.getCreatedBy());
        jgen.writeStringField(SCOPE_ID, reportDTO.getScopeName());
        jgen.writeStringField(OUT_COMPONENTS, reportDTO.getOutComponents());
        jgen.writeFieldName(FILTER_EXPRESSION);
    }

    protected void writeCommon(JsonGenerator jgen, CommonFilterDTO commonFilter) throws IOException {
        jgen.writeFieldName("common");
        jgen.writeStartObject();

        PositionSelectorDTO positionSelector = commonFilter.getPositionSelector();
        switch (positionSelector.getSelector()){
            case ALL :
                jgen.writeStringField("startDate", DateUtils.dateToString(commonFilter.getStartDate()));
                jgen.writeStringField("endDate", DateUtils.dateToString(commonFilter.getEndDate()));
                jgen.writeStringField("positionSelector", positionSelector.getSelector().name());
                break;
        }
        jgen.writeEndObject();
    }
}
