/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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