package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mapProjection",
        "displayProjection",
        "coordinatesFormat",
        "scaleBarUnits"
})
public class MapConfigurationDTO {

    @JsonProperty("mapProjection")
    private Long mapProjection;
    @JsonProperty("displayProjection")
    private Long displayProjection;
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
    public MapConfigurationDTO(Long mapProjection, Long displayProjection, String coordinatesFormat, String scaleBarUnits) {
        this.mapProjection = mapProjection;
        this.displayProjection = displayProjection;
        this.coordinatesFormat = coordinatesFormat;
        this.scaleBarUnits = scaleBarUnits;
    }

    @JsonProperty("mapProjection")
    public Long getMapProjection() {
        return mapProjection;
    }

    @JsonProperty("mapProjection")
    public void setMapProjection(Long mapProjection) {
        this.mapProjection = mapProjection;
    }

    public MapConfigurationDTO withMapProjection(Long mapProjection) {
        this.mapProjection = mapProjection;
        return this;
    }

    @JsonProperty("displayProjection")
    public Long getDisplayProjection() {
        return displayProjection;
    }

    @JsonProperty("displayProjection")
    public void setDisplayProjection(Long displayProjection) {
        this.displayProjection = displayProjection;
    }

    public MapConfigurationDTO withDisplayProjection(Long displayProjection) {
        this.displayProjection = displayProjection;
        return this;
    }

    @JsonProperty("coordinatesFormat")
    public String getCoordinatesFormat() {
        return coordinatesFormat;
    }

    @JsonProperty("coordinatesFormat")
    public void setCoordinatesFormat(String coordinatesFormat) {
        this.coordinatesFormat = coordinatesFormat;
    }

    public MapConfigurationDTO withCoordinatesFormat(String coordinatesFormat) {
        this.coordinatesFormat = coordinatesFormat;
        return this;
    }

    @JsonProperty("scaleBarUnits")
    public String getScaleBarUnits() {
        return scaleBarUnits;
    }

    @JsonProperty("scaleBarUnits")
    public void setScaleBarUnits(String scaleBarUnits) {
        this.scaleBarUnits = scaleBarUnits;
    }

    public MapConfigurationDTO withScaleBarUnits(String scaleBarUnits) {
        this.scaleBarUnits = scaleBarUnits;
        return this;
    }

}
