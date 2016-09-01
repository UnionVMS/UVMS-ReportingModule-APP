/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.io.Serializable;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "spatialConnectId",
        "mapProjectionId",
        "displayProjectionId",
        "coordinatesFormat",
        "scaleBarUnits",
        "visibilitySettings",
        "styleSettings",
        "layerSettings"
})
public class MapConfigurationDTO implements Serializable{

    @JsonProperty("spatialConnectId")
    private Long spatialConnectId;
    @JsonProperty("mapProjectionId")
    private Long mapProjectionId;
    @JsonProperty("displayProjectionId")
    private Long displayProjectionId;
    @JsonProperty("coordinatesFormat")
    private String coordinatesFormat;
    @JsonProperty("scaleBarUnits")
    private String scaleBarUnits;

    @JsonProperty("visibilitySettings")
    private VisibilitySettingsDto visibilitySettings;

    @JsonProperty("styleSettings")
    private StyleSettingsDto styleSettings;

    @JsonProperty("layerSettings")
    private LayerSettingsDto layerSettings;

    @JsonProperty("referenceDataSettings")
    private Map<String, ReferenceDataPropertiesDto> referenceData;


    /**
     * No args constructor for use in serialization
     */
    public MapConfigurationDTO() {
    }

    @Builder(builderMethodName = "MapConfigurationDTOBuilder")
    public MapConfigurationDTO(Long spatialConnectId,
                               Long mapProjectionId,
                               Long displayProjectionId,
                               String coordinatesFormat,
                               String scaleBarUnits,
                               VisibilitySettingsDto visibilitySettings,
                               StyleSettingsDto styleSettings,
                               LayerSettingsDto layerSettings,
                               Map<String, ReferenceDataPropertiesDto> referenceData) {
        this.spatialConnectId = spatialConnectId;
        this.mapProjectionId = mapProjectionId;
        this.displayProjectionId = displayProjectionId;
        this.coordinatesFormat = coordinatesFormat;
        this.scaleBarUnits = scaleBarUnits;
        this.visibilitySettings = visibilitySettings;
        this.styleSettings = styleSettings;
        this.layerSettings = layerSettings;
        this.referenceData = referenceData;
    }

    @JsonProperty("mapProjectionId")
    public Long getMapProjectionId() {
        return mapProjectionId;
    }

    @JsonProperty("mapProjectionId")
    public void setMapProjectionId(Long mapProjectionId) {
        this.mapProjectionId = mapProjectionId;
    }

    @JsonProperty("displayProjectionId")
    public Long getDisplayProjectionId() {
        return displayProjectionId;
    }

    @JsonProperty("displayProjectionId")
    public void setDisplayProjectionId(Long displayProjectionId) {
        this.displayProjectionId = displayProjectionId;
    }

    @JsonProperty("coordinatesFormat")
    public String getCoordinatesFormat() {
        return coordinatesFormat;
    }

    @JsonProperty("coordinatesFormat")
    public void setCoordinatesFormat(String coordinatesFormat) {
        this.coordinatesFormat = coordinatesFormat;
    }

    @JsonProperty("scaleBarUnits")
    public String getScaleBarUnits() {
        return scaleBarUnits;
    }

    @JsonProperty("scaleBarUnits")
    public void setScaleBarUnits(String scaleBarUnits) {
        this.scaleBarUnits = scaleBarUnits;
    }

    @JsonProperty("spatialConnectId")
    public Long getSpatialConnectId() {
        return spatialConnectId;
    }

    @JsonProperty("spatialConnectId")
    public void setSpatialConnectId(Long spatialConnectId) {
        this.spatialConnectId = spatialConnectId;
    }

    @JsonProperty("visibilitySettings")
    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    @JsonProperty("visibilitySettings")
    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }

    @JsonProperty("styleSettings")
    public StyleSettingsDto getStyleSettings() {
        return styleSettings;
    }

    @JsonProperty("styleSettings")
    public void setStyleSettings(StyleSettingsDto styleSettings) {
        this.styleSettings = styleSettings;
    }

    @JsonProperty("layerSettings")
    public LayerSettingsDto getLayerSettings() {
        return layerSettings;
    }

    @JsonProperty("layerSettings")
    public void setLayerSettings(LayerSettingsDto layerSettings) {
        this.layerSettings = layerSettings;
    }

    @JsonProperty("referenceDataSettings")
    public Map<String, ReferenceDataPropertiesDto> getReferenceData() {
        return referenceData;
    }

    @JsonProperty("referenceDataSettings")
    public void setReferenceData(Map<String, ReferenceDataPropertiesDto> referenceData) {
        this.referenceData = referenceData;
    }

}