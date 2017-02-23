/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.mapper.VmsTrackFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TrackFilterDTO extends FilterDTO {

    public static final String TRK_MIN_TIME = "trkMinTime";
    public static final String TRK_MAX_TIME = "trkMaxTime";
    public static final String TRK_MIN_DURATION = "trkMinDuration";
    public static final String TRK_MAX_DURATION = "trkMaxDuration";
    public static final String TRACKS = "tracks";

    @JsonProperty(TRK_MAX_TIME)
    private Float maxTime;

    @JsonProperty(TRK_MIN_TIME)
    private Float minTime;

    @JsonProperty(TRK_MIN_DURATION)
    private Float minDuration;

    @JsonProperty(TRK_MAX_DURATION)
    private Float maxDuration;

    private Float minDistance;

    private Float maxDistance;

    private Float minAvgSpeed;

    private Float maxAvgSpeed;

    public TrackFilterDTO() {
        super(FilterType.vmstrack);
    }

    public TrackFilterDTO(Long id, Long reportId) {
        super(FilterType.vmstrack, id, reportId);
    }

    @Builder
    public TrackFilterDTO(Long id,
                          Long reportId,
                          Float maxTime,
                          Float minTime,
                          Float minDuration,
                          Float maxDuration,
                          Float minDistance,
                          Float maxDistance,
                          Float minAvgSpeed,
                          Float maxAvgSpeed) {
        this(id, reportId);
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minAvgSpeed = minAvgSpeed;
        this.maxAvgSpeed = maxAvgSpeed;
        validate();
    }

    @Override
    public Filter convertToFilter() {
        return VmsTrackFilterMapper.INSTANCE.trackFilterDTOToTrackFilter(this);
    }

    public Float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Float maxTime) {
        this.maxTime = maxTime;
    }

    public Float getMinTime() {
        return minTime;
    }

    public void setMinTime(Float minTime) {
        this.minTime = minTime;
    }

    public Float getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Float minDuration) {
        this.minDuration = minDuration;
    }

    public Float getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Float maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(Float minDistance) {
        this.minDistance = minDistance;
    }

    public Float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Float getMinAvgSpeed() {
        return minAvgSpeed;
    }

    public void setMinAvgSpeed(Float minAvgSpeed) {
        this.minAvgSpeed = minAvgSpeed;
    }

    public Float getMaxAvgSpeed() {
        return maxAvgSpeed;
    }

    public void setMaxAvgSpeed(Float maxAvgSpeed) {
        this.maxAvgSpeed = maxAvgSpeed;
    }
}