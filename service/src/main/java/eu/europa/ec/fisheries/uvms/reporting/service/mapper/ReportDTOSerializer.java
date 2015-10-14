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
import java.util.Date;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.*;

public class ReportDTOSerializer extends JsonSerializer<ReportDTO> {

    @Override
    public void serialize(ReportDTO reportDTO, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {

        List<FilterDTO> filters = reportDTO.getFilters();

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

    private void serializeFilterFields(JsonGenerator jgen, List<FilterDTO> filters) throws IOException {

        VmsPositionFilterDTO position = null;
        VmsSegmentFilterDTO segment = null;
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
                case vmsseg:
                    segment = (VmsSegmentFilterDTO) filterDTO;
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

        jgen.writeFieldName("vms");
            jgen.writeStartObject();
            writeVmsPosition(jgen, position);
            writeVmsTrack(jgen, track);
            writeVmsSegments(jgen, segment);
        jgen.writeEndObject();

        jgen.writeEndObject();
    }

    private void writeVmsTrack(JsonGenerator jgen, TrackFilterDTO track) throws IOException {
        if (track != null){
            jgen.writeFieldName(TrackFilterDTO.TRACKS);
            jgen.writeObject(track);
        }
    }

    private void writeVmsSegments(JsonGenerator jgen, VmsSegmentFilterDTO vmsSegmentFilterDTO) throws IOException {
        if (vmsSegmentFilterDTO != null){
            jgen.writeFieldName(VmsSegmentFilterDTO.VMS_SEGMENT);
            jgen.writeObject(vmsSegmentFilterDTO);
        }
    }

    private void writeVessels(JsonGenerator jgen, List<FilterDTO> vesselFilterDTOList) throws IOException {
        if(CollectionUtils.isNotEmpty(vesselFilterDTOList)) {
            jgen.writeObjectField(VesselFilterDTO.VESSELS, vesselFilterDTOList);
        }
    }

    private void writeVmsPosition(JsonGenerator jgen, VmsPositionFilterDTO position) throws IOException {
        if (position != null){
            jgen.writeFieldName(VmsPositionFilterDTO.VMS_POSITION);
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
        if(commonFilter != null){
            jgen.writeFieldName(CommonFilterDTO.COMMON);
            jgen.writeStartObject();
            Long id = commonFilter.getId();
            if (id != null){
                jgen.writeNumberField(FilterDTO.ID, commonFilter.getId());
            }
            PositionSelectorDTO positionSelector = commonFilter.getPositionSelector();
            jgen.writeStringField(CommonFilterDTO.POSITION_SELECTOR, positionSelector.getSelector().getName());
            Date startDate = commonFilter.getStartDate();
            Date endDate = commonFilter.getEndDate();
            if (startDate != null){
                jgen.writeStringField(CommonFilterDTO.START_DATE,UI_FORMATTER.print(new DateTime(startDate)));

            }
            if (endDate != null){
                jgen.writeStringField(CommonFilterDTO.END_DATE,UI_FORMATTER.print(new DateTime(endDate)));
            }
            switch (positionSelector.getSelector()){
                case LAST:
                    jgen.writeStringField(PositionSelectorDTO.POSITION_TYPE_SELECTOR, positionSelector.getPosition().getName());
                    jgen.writeNumberField(PositionSelectorDTO.VALUE, positionSelector.getValue());
                    break;
            }

            jgen.writeEndObject();
        }
    }
}
