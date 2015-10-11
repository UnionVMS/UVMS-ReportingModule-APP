package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import org.apache.commons.collections4.CollectionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.*;

/**
 * //TODO create test
 */
public class ReportDTOSerializer extends JsonSerializer<ReportDTO> {

    @Override
    public void serialize(ReportDTO reportDTO, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {

        Set<FilterDTO> filters = reportDTO.getFilters();

        jgen.writeStartObject();

        serializeReportFields(reportDTO, jgen);

        if (filters != null) {
            serializeFilterFields(jgen, filters);
        }
        else {
            serializeAccessFields(reportDTO, jgen);
        }

        jgen.writeEndObject();

    }

    private void serializeAccessFields(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        jgen.writeBooleanField(EDITABLE, reportDTO.isEditable());
        jgen.writeBooleanField(SHAREABLE, reportDTO.isShareable());
        jgen.writeBooleanField(DELETABLE, reportDTO.isDeletable());
    }

    private void serializeFilterFields(JsonGenerator jgen, Set<FilterDTO> filters) throws IOException {

        FilterDTO position = null;
        FilterDTO segment = null;
        FilterDTO track = null;

        List<FilterDTO> vesselFilterDTOList = new ArrayList<>();
        CommonFilterDTO commonFilter = null;

        for (FilterDTO filterDTO : filters) {
            FilterType type = filterDTO.getType();
            switch (type) {
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
        jgen.writeFieldName(FILTER_EXPRESSION);
        jgen.writeStartObject();
        writeCommonFields(jgen, commonFilter);
        writeVessels(jgen, vesselFilterDTOList);
        writeVms(jgen, position);
        jgen.writeEndObject();
    }

    private void writeVessels(JsonGenerator jgen, List<FilterDTO> vesselFilterDTOList) throws IOException {
        if(CollectionUtils.isNotEmpty(vesselFilterDTOList)) {
            jgen.writeObjectField("vessels", vesselFilterDTOList);
        }
    }

    private void writeVms(JsonGenerator jgen, FilterDTO position) throws IOException {
        jgen.writeFieldName("vms");
        if (position != null){
            jgen.writeObject(position);
        }
    }

    private void serializeReportFields(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        jgen.writeNumberField(ID, reportDTO.getId());
        jgen.writeStringField(NAME, reportDTO.getName());
        jgen.writeStringField(DESC, reportDTO.getDescription());
        jgen.writeStringField(VISIBILITY, reportDTO.getVisibility().name());
        jgen.writeStringField(CREATED_ON, DateUtils.dateToString(reportDTO.getAudit().getCreatedOn()));
        jgen.writeStringField(CREATED_BY, reportDTO.getCreatedBy());
        jgen.writeStringField(SCOPE_ID, reportDTO.getScopeName());
        jgen.writeStringField(OUT_COMPONENTS, reportDTO.getOutComponents());
    }

    private void writeCommonFields(JsonGenerator jgen, CommonFilterDTO commonFilter) throws IOException {
        jgen.writeFieldName("common");
        jgen.writeStartObject();
        jgen.writeNumberField(FilterDTO.ID, commonFilter.getId());
        PositionSelectorDTO positionSelector = commonFilter.getPositionSelector();
        jgen.writeStringField(CommonFilterDTO.POSITION_SELECTOR, positionSelector.getSelector().name());

        switch (positionSelector.getSelector()){
            case ALL :
                jgen.writeStringField(CommonFilterDTO.START_DATE, DateUtils.dateToString(commonFilter.getStartDate()));
                jgen.writeStringField(CommonFilterDTO.END_DATE, DateUtils.dateToString(commonFilter.getEndDate()));
                jgen.writeStringField(CommonFilterDTO.POSITION_SELECTOR, positionSelector.getSelector().name());
                break;
            case LAST:
                jgen.writeNumberField(PositionSelectorDTO.VALUE, positionSelector.getValue());
                break;

        }
        jgen.writeEndObject();
    }
}
