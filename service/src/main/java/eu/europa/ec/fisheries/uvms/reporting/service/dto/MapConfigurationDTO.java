package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "spatialConnectId",
        "mapProjectionId",
        "displayProjectionId",
        "coordinatesFormat",
        "scaleBarUnits"
})
public class MapConfigurationDTO {

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


    /**
     * No args constructor for use in serialization
     */
    public MapConfigurationDTO() {
    }

    @Builder(builderMethodName = "MapConfigurationDTOBuilder")
    public MapConfigurationDTO(Long spatialConnectId, Long mapProjectionId, Long displayProjectionId, String coordinatesFormat, String scaleBarUnits) {
        this.spatialConnectId = spatialConnectId;
        this.mapProjectionId = mapProjectionId;
        this.displayProjectionId = displayProjectionId;
        this.coordinatesFormat = coordinatesFormat;
        this.scaleBarUnits = scaleBarUnits;
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

}
