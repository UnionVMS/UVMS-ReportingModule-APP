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
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.*;

public class ReportDTOSerializer extends JsonSerializer<ReportDTO> {

    @Override
    public void serialize(ReportDTO reportDTO, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {

        List<FilterDTO> filters = reportDTO.getFilters();

        jgen.writeStartObject();

        serializeReportFields(reportDTO, jgen);

        if (filters != null && !filters.isEmpty()) {
            serializeFilterFields(jgen, filters);
        } else {
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
        jgen.writeFieldName(FILTER_EXPRESSION);
        jgen.writeStartObject();

        List<FilterDTO> vesselFilterDTOList = new ArrayList<>();
        List<FilterDTO> areaFilterDTOList = new ArrayList<>();
        VmsPositionFilterDTO position = null;
        VmsSegmentFilterDTO segment = null;
        TrackFilterDTO track = null;
        CommonFilterDTO commonFilter = null;

        for (FilterDTO filterDTO : filters) {
            FilterType type = filterDTO.getType();
            switch (type) {
                case areas:
                    areaFilterDTOList.add(filterDTO);
                    break;
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
                    vesselFilterDTOList.add(filterDTO);
                    break;
                case vgroup:
                    vesselFilterDTOList.add(filterDTO);
                    break;
            }
        }
        writeCommonFields(jgen, commonFilter);

        jgen.writeFieldName("vms");
        jgen.writeStartObject();
        writeVmsPosition(jgen, position);
        writeVmsTrack(jgen, track);
        writeVmsSegments(jgen, segment);
        jgen.writeEndObject();

        writeAreaFilters(jgen, areaFilterDTOList);
        writeVessels(jgen, vesselFilterDTOList);
        jgen.writeEndObject();
    }


    private void writeAreaFilters(JsonGenerator jgen, List<FilterDTO> areaFilterDTOList) throws IOException {
        jgen.writeObjectField(FilterType.areas.toString(), areaFilterDTOList);
    }

    private void writeVmsTrack(JsonGenerator jgen, TrackFilterDTO track) throws IOException {
        if (track != null) {
            jgen.writeFieldName(TrackFilterDTO.TRACKS);
            jgen.writeObject(track);
        }
    }

    private void writeVmsSegments(JsonGenerator jgen, VmsSegmentFilterDTO vmsSegmentFilterDTO) throws IOException {
        if (vmsSegmentFilterDTO != null) {
            jgen.writeFieldName(VmsSegmentFilterDTO.VMS_SEGMENT);
            jgen.writeObject(vmsSegmentFilterDTO);
        }
    }

    private void writeVessels(JsonGenerator jgen, List<FilterDTO> vesselFilterDTOList) throws IOException {
        jgen.writeObjectField(VesselFilterDTO.VESSELS, vesselFilterDTOList);
    }

    private void writeVmsPosition(JsonGenerator jgen, VmsPositionFilterDTO position) throws IOException {
        if (position != null) {
            jgen.writeFieldName(VmsPositionFilterDTO.VMS_POSITION);
            jgen.writeObject(position);
        }
    }

    private void serializeReportFields(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        jgen.writeNumberField(ID, reportDTO.getId());
        jgen.writeStringField(NAME, reportDTO.getName());
        if (reportDTO.getDescription() != null) {
            jgen.writeStringField(DESC, reportDTO.getDescription());
        }
        jgen.writeStringField(VISIBILITY, reportDTO.getVisibility().getName());
        jgen.writeStringField(CREATED_ON, UI_FORMATTER.print(new DateTime(reportDTO.getAudit().getCreatedOn())));

        Set<ExecutionLogDTO> executionLogDTOs = reportDTO.filterLogsByUser(reportDTO.getCreatedBy());
        if (CollectionUtils.isNotEmpty(executionLogDTOs)) {
            ExecutionLogDTO userExecutionLog = executionLogDTOs.iterator().next();
            String executedOn = UI_FORMATTER.print(new DateTime(userExecutionLog.getExecutedOn()));
            jgen.writeStringField(ExecutionLogDTO.EXECUTED_ON, executedOn);
        } else {
            jgen.writeStringField(ExecutionLogDTO.EXECUTED_ON, null);
        }
        jgen.writeStringField(CREATED_BY, reportDTO.getCreatedBy());
        jgen.writeBooleanField(WITH_MAP, reportDTO.getWithMap());
    }

    private void writeCommonFields(JsonGenerator jgen, CommonFilterDTO commonFilter) throws IOException {
        if (commonFilter != null) {
            jgen.writeFieldName(CommonFilterDTO.COMMON);
            jgen.writeStartObject();
            Long id = commonFilter.getId();
            if (id != null) {
                jgen.writeNumberField(FilterDTO.ID, commonFilter.getId());
            }
            PositionSelectorDTO positionSelector = commonFilter.getPositionSelector();
            if (positionSelector != null) {
                jgen.writeStringField(CommonFilterDTO.POSITION_SELECTOR, positionSelector.getSelector().name());
                switch (positionSelector.getSelector()) {
                    case last:
                        jgen.writeStringField(PositionSelectorDTO.POSITION_TYPE_SELECTOR, positionSelector.getPosition().toString());
                        jgen.writeNumberField(PositionSelectorDTO.X_VALUE, positionSelector.getValue());
                        break;
                }
            }
            Date startDate = commonFilter.getStartDate();
            Date endDate = commonFilter.getEndDate();
            if (startDate != null) {
                jgen.writeStringField(CommonFilterDTO.START_DATE, UI_FORMATTER.print(new DateTime(startDate)));

            }
            if (endDate != null) {
                jgen.writeStringField(CommonFilterDTO.END_DATE, UI_FORMATTER.print(new DateTime(endDate)));
            }

            jgen.writeEndObject();
        }
    }
}
