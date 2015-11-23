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
    private Integer mapProjection;
    @JsonProperty("displayProjection")
    private Integer displayProjection;
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
    public MapConfigurationDTO(Integer mapProjection, Integer displayProjection, String coordinatesFormat, String scaleBarUnits) {
        this.mapProjection = mapProjection;
        this.displayProjection = displayProjection;
        this.coordinatesFormat = coordinatesFormat;
        this.scaleBarUnits = scaleBarUnits;
    }

    @JsonProperty("mapProjection")
    public Integer getMapProjection() {
        return mapProjection;
    }

    @JsonProperty("mapProjection")
    public void setMapProjection(Integer mapProjection) {
        this.mapProjection = mapProjection;
    }

    public MapConfigurationDTO withMapProjection(Integer mapProjection) {
        this.mapProjection = mapProjection;
        return this;
    }

    @JsonProperty("displayProjection")
    public Integer getDisplayProjection() {
        return displayProjection;
    }

    @JsonProperty("displayProjection")
    public void setDisplayProjection(Integer displayProjection) {
        this.displayProjection = displayProjection;
    }

    public MapConfigurationDTO withDisplayProjection(Integer displayProjection) {
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
