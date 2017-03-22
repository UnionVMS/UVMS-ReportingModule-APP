/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.*;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 12/6/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TripDTO {

    private String tripId;

    private String schemeId;

    @JsonProperty("multipointWkt")
    private String geometry;

    private String firstFishingActivity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date firstFishingActivityDateTime;

    private String lastFishingActivity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date lastFishingActivityDateTime;

    private Integer noOfCorrections;

    private Double tripDuration;

    private String flagState;

    private Integer vmsPositionCount;

    @JsonIgnore
    private List<VesselIdentifierType> vesselIdLists;

    @JsonIgnore
    private Date relativeFirstFaDateTime;

    @JsonIgnore
    private Date relativeLastFaDateTime;

    public TripDTO() {
    }

    public TripDTO(String tripId, String schemeId, String geometry) {
        this.tripId = tripId;
        this.schemeId = schemeId;
        this.geometry = geometry;
    }

    @JsonProperty("vesselIds")
    public Map<String, String> generateVesselIdMap() {
        Map<String, String> vesselIds = new HashMap<>();
        if (vesselIdLists != null && !vesselIdLists.isEmpty()) {
            for (VesselIdentifierType vesselIdentifierType : vesselIdLists) {
                vesselIds.put(vesselIdentifierType.getKey().value(), vesselIdentifierType.getValue());
            }
            return vesselIds;
        }
        return null;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getFirstFishingActivity() {
        return firstFishingActivity;
    }

    public void setFirstFishingActivity(String firstFishingActivity) {
        this.firstFishingActivity = firstFishingActivity;
    }

    public Date getFirstFishingActivityDateTime() {
        return firstFishingActivityDateTime;
    }

    public void setFirstFishingActivityDateTime(Date firstFishingActivityDateTime) {
        this.firstFishingActivityDateTime = firstFishingActivityDateTime;
    }

    public String getLastFishingActivity() {
        return lastFishingActivity;
    }

    public void setLastFishingActivity(String lastFishingActivity) {
        this.lastFishingActivity = lastFishingActivity;
    }

    public Date getLastFishingActivityDateTime() {
        return lastFishingActivityDateTime;
    }

    public void setLastFishingActivityDateTime(Date lastFishingActivityDateTime) {
        this.lastFishingActivityDateTime = lastFishingActivityDateTime;
    }

    public Integer getNoOfCorrections() {
        return noOfCorrections;
    }

    public void setNoOfCorrections(Integer noOfCorrections) {
        this.noOfCorrections = noOfCorrections;
    }

    public Double getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(Double tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getFlagState() {
        return flagState;
    }

    public void setFlagState(String flagState) {
        this.flagState = flagState;
    }

    public Integer getVmsPositionCount() {
        return vmsPositionCount;
    }

    public void setVmsPositionCount(Integer vmsPositionCount) {
        this.vmsPositionCount = vmsPositionCount;
    }

    public List<VesselIdentifierType> getVesselIdLists() {
        return vesselIdLists;
    }

    public void setVesselIdLists(List<VesselIdentifierType> vesselIdLists) {
        this.vesselIdLists = vesselIdLists;
    }

    public Date getRelativeFirstFaDateTime() {
        return relativeFirstFaDateTime;
    }

    public void setRelativeFirstFaDateTime(Date relativeFirstFaDateTime) {
        this.relativeFirstFaDateTime = relativeFirstFaDateTime;
    }

    public Date getRelativeLastFaDateTime() {
        return relativeLastFaDateTime;
    }

    public void setRelativeLastFaDateTime(Date relativeLastFaDateTime) {
        this.relativeLastFaDateTime = relativeLastFaDateTime;
    }
}
