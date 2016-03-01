package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by padhyad on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilitySettingsDto {

    @NotNull
    @JsonProperty("positions")
    private VisibilityPositionsDto visibilityPositionsDto;

    @NotNull
    @JsonProperty("segments")
    private VisibilitySegmentDto visibilitySegmentDto;

    @NotNull
    @JsonProperty("tracks")
    private VisibilityTracksDto visibilityTracksDto;

    public VisibilitySettingsDto() {}

    public VisibilitySettingsDto(VisibilityPositionsDto visibilityPositionsDto, VisibilitySegmentDto visibilitySegmentDto, VisibilityTracksDto visibilityTracksDto) {
        this.visibilityPositionsDto = visibilityPositionsDto;
        this.visibilitySegmentDto = visibilitySegmentDto;
        this.visibilityTracksDto = visibilityTracksDto;
    }

    @JsonProperty("positions")
    public VisibilityPositionsDto getVisibilityPositionsDto() {
        return visibilityPositionsDto;
    }

    @JsonProperty("positions")
    public void setVisibilityPositionsDto(VisibilityPositionsDto visibilityPositionsDto) {
        this.visibilityPositionsDto = visibilityPositionsDto;
    }

    @JsonProperty("segments")
    public VisibilitySegmentDto getVisibilitySegmentDto() {
        return visibilitySegmentDto;
    }

    @JsonProperty("segments")
    public void setVisibilitySegmentDto(VisibilitySegmentDto visibilitySegmentDto) {
        this.visibilitySegmentDto = visibilitySegmentDto;
    }

    @JsonProperty("tracks")
    public VisibilityTracksDto getVisibilityTracksDto() {
        return visibilityTracksDto;
    }

    @JsonProperty("tracks")
    public void setVisibilityTracksDto(VisibilityTracksDto visibilityTracksDto) {
        this.visibilityTracksDto = visibilityTracksDto;
    }
}
