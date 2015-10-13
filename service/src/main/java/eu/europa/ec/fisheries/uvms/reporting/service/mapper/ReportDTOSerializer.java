package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.*;

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

        VmsPositionFilterDTO position = null;
        FilterDTO segment = null;
        TrackFilterDTO track = null;

        List<FilterDTO> vesselFilterDTOList = new ArrayList<>();
        CommonFilterDTO commonFilter = null;

        for (FilterDTO filterDTO : filters) {
            FilterType type = filterDTO.getType();
            switch (type) {
                case common:
                    commonFilter = (CommonFilterDTO) filterDTO;
                    break;
                case vmspos:
                    position = (VmsPositionFilterDTO) filterDTO;
                    break;
                case vmstrack:
                    track = (TrackFilterDTO) filterDTO;
                    break;
                case vessel:
                case vgroup:
                    vesselFilterDTOList.add(filterDTO);
                    break;
            }
        }
        jgen.writeFieldName(FILTER_EXPRESSION);
        jgen.writeStartObject();
        writeCommonFields(jgen, commonFilter);
        writeVessels(jgen, vesselFilterDTOList);
        writeVmsPosition(jgen, position);
        writeVmsTrack(jgen, track);
        jgen.writeEndObject();
    }

    private void writeVmsTrack(JsonGenerator jgen, TrackFilterDTO track) throws IOException {
        if (track != null){
            jgen.writeFieldName(TrackFilterDTO.TRACKS);
            jgen.writeObject(track);
        }
    }

    private void writeVessels(JsonGenerator jgen, List<FilterDTO> vesselFilterDTOList) throws IOException {
        if(CollectionUtils.isNotEmpty(vesselFilterDTOList)) {
            jgen.writeObjectField(VesselFilterDTO.VESSEL, vesselFilterDTOList);
        }
    }

    private void writeVmsPosition(JsonGenerator jgen, VmsPositionFilterDTO position) throws IOException {
        if (position != null){
            jgen.writeFieldName(VmsPositionFilterDTO.VMS);
            jgen.writeObject(position);
        }
    }

    private void serializeReportFields(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        jgen.writeNumberField(ID, reportDTO.getId());
        jgen.writeStringField(NAME, reportDTO.getName());
        if (reportDTO.getDescription() != null){
            jgen.writeStringField(DESC, reportDTO.getDescription());
        }
        jgen.writeStringField(VISIBILITY, reportDTO.getVisibility().getName());
        jgen.writeStringField(CREATED_ON, UI_FORMATTER.print(new DateTime(reportDTO.getAudit().getCreatedOn())));
        jgen.writeStringField(CREATED_BY, reportDTO.getCreatedBy());
        jgen.writeBooleanField(WITH_MAP, reportDTO.getWithMap());
    }

    private void writeCommonFields(JsonGenerator jgen, CommonFilterDTO commonFilter) throws IOException {
        jgen.writeFieldName(CommonFilterDTO.COMMON);
        jgen.writeStartObject();
        jgen.writeNumberField(FilterDTO.ID, commonFilter.getId());
        PositionSelectorDTO positionSelector = commonFilter.getPositionSelector();
        jgen.writeStringField(CommonFilterDTO.POSITION_SELECTOR, positionSelector.getSelector().getName());

        switch (positionSelector.getSelector()){
            case ALL :
                jgen.writeStringField(CommonFilterDTO.START_DATE,
                        UI_FORMATTER.print(new DateTime(commonFilter.getStartDate())));
                jgen.writeStringField(CommonFilterDTO.END_DATE,
                        UI_FORMATTER.print(new DateTime(commonFilter.getEndDate())));
                break;
            case LAST:
                jgen.writeNumberField(PositionSelectorDTO.VALUE, positionSelector.getValue());
                break;

        }
        jgen.writeEndObject();
    }
}
