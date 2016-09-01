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

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StyleSettingsDto implements Serializable{

    @NotNull
    @JsonProperty("positions")
    private PositionsDto positions;

    @NotNull
    @JsonProperty("segments")
    private SegmentsDto segments;

    @JsonProperty("alarms")
    private AlarmsDto alarms;

    public StyleSettingsDto() {}

    public StyleSettingsDto(PositionsDto positions, SegmentsDto segments) {
        this.positions = positions;
        this.segments = segments;
    }

    @JsonProperty("positions")
    public PositionsDto getPositions() {
        return positions;
    }

    @JsonProperty("positions")
    public void setPositions(PositionsDto positionsDto) {
        this.positions = positionsDto;
    }

    @JsonProperty("segments")
    public SegmentsDto getSegments() {
        return segments;
    }

    @JsonProperty("segments")
    public void setSegments(SegmentsDto segmentsDto) {
        this.segments = segmentsDto;
    }

    @JsonProperty("alarms")
    public AlarmsDto getAlarms() {
        return alarms;
    }

    @JsonProperty("alarms")
    public void setAlarms(AlarmsDto alarms) {
        this.alarms = alarms;
    }

    @Override
    public String toString() {
        return "ClassPojo [positions = " + positions + ", segments = " + segments + "]";
    }
}