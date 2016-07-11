/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
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

        if (filters != null && !filters.isEmpty()) {
            serializeFilterFields(jgen, filters);
        } else {
            serializeAccessFields(reportDTO, jgen);
        }

        jgen.writeEndObject();

    }

    private void serializeAccessFields(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        jgen.writeBooleanField(EDITABLE, reportDTO.isEditable());
        if (reportDTO.isShareable() != null) {
            jgen.writeArrayFieldStart(SHAREABLE);
            for (VisibilityEnum visibilityEnum : reportDTO.isShareable()) {
                jgen.writeString(visibilityEnum.toString());
            }
            jgen.writeEndArray();
        }
        jgen.writeBooleanField(DELETABLE, reportDTO.isDeletable());
    }

    private void serializeFilterFields(JsonGenerator jgen, List<FilterDTO> filters) throws IOException {
        jgen.writeFieldName(FILTER_EXPRESSION);
        jgen.writeStartObject();

        List<FilterDTO> assetFilterDTOList = new ArrayList<>();
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
                case asset:
                    assetFilterDTOList.add(filterDTO);
                    break;
                case vgroup:
                    assetFilterDTOList.add(filterDTO);
                    break;
                default:
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
        writeAssets(jgen, assetFilterDTOList);
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

    private void writeAssets(JsonGenerator jgen, List<FilterDTO> assetFilterDTOList) throws IOException {
        jgen.writeObjectField(AssetFilterDTO.ASSETS, assetFilterDTOList);
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
        ExecutionLogDTO executionLog = reportDTO.getExecutionLog();
        if (executionLog != null) {
            String executedOn = UI_FORMATTER.print(new DateTime(executionLog.getExecutedOn()));
            jgen.writeStringField(ExecutionLogDTO.EXECUTED_ON, executedOn);
        } else {
            jgen.writeStringField(ExecutionLogDTO.EXECUTED_ON, null);
        }
        jgen.writeStringField(CREATED_BY, reportDTO.getCreatedBy());
        jgen.writeBooleanField(WITH_MAP, reportDTO.getWithMap());
        jgen.writeBooleanField(IS_DEFAULT, reportDTO.isDefault());

        writeMapConfigFileds(reportDTO, jgen);
    }

    private void writeMapConfigFileds(ReportDTO reportDTO, JsonGenerator jgen) throws IOException {
        if (reportDTO.getWithMap() && (reportDTO.getMapConfiguration() != null)) {
            jgen.writeFieldName(MAP_CONFIGURATION);
            jgen.writeStartObject();
            MapConfigurationDTO mapConfiguration = reportDTO.getMapConfiguration();
            if (mapConfiguration.getSpatialConnectId() != null) {
                jgen.writeNumberField("spatialConnectId", mapConfiguration.getSpatialConnectId());
            }
            if (mapConfiguration.getMapProjectionId() != null) {
                jgen.writeNumberField("mapProjectionId", mapConfiguration.getMapProjectionId());
            }
            if (mapConfiguration.getDisplayProjectionId() != null) {
                jgen.writeNumberField("displayProjectionId", mapConfiguration.getDisplayProjectionId());
            }
            if (mapConfiguration.getScaleBarUnits() != null) {
                jgen.writeStringField("scaleBarUnits", mapConfiguration.getScaleBarUnits());
            }
            if (mapConfiguration.getCoordinatesFormat() != null) {
                jgen.writeStringField("coordinatesFormat", mapConfiguration.getCoordinatesFormat());
            }
            if (mapConfiguration.getVisibilitySettings() != null) {
                jgen.writeFieldName("visibilitySettings");
                jgen.writeObject(mapConfiguration.getVisibilitySettings());
            }
            if (mapConfiguration.getStyleSettings() != null) {
                jgen.writeFieldName("stylesSettings");
                jgen.writeObject(mapConfiguration.getStyleSettings());
            }
            if (mapConfiguration.getLayerSettings() != null) {
                jgen.writeFieldName("layerSettings");
                jgen.writeObject(mapConfiguration.getLayerSettings());
            }
            if (mapConfiguration.getReferenceData() != null) {
                jgen.writeFieldName("referenceDataSettings");
                jgen.writeObject(mapConfiguration.getReferenceData());
            }
            jgen.writeEndObject();
        }
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
                    default:
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