/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;

public class ExecutionResultDTO {

    private DefaultFeatureCollection movements = new DefaultFeatureCollection(null, MovementDTO.SIMPLE_FEATURE_TYPE);
    private DefaultFeatureCollection segments = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);
    private DefaultFeatureCollection activities = new DefaultFeatureCollection(null, ActivityDTO.ACTIVITY);
    private List<TrackDTO> tracks = new ArrayList<>();
    private List<TripDTO> trips = new ArrayList<>();
    private List<FishingActivitySummaryDTO> activityList;
    private FACatchSummaryDTO faCatchSummaryDTO;

    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }

    public List<FishingActivitySummaryDTO> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<FishingActivitySummaryDTO> activityList) {
        this.activityList = activityList;
    }

    public FACatchSummaryDTO getFaCatchSummaryDTO() {
        return faCatchSummaryDTO;
    }

    public void setFaCatchSummaryDTO(FACatchSummaryDTO faCatchSummaryDTO) {
        this.faCatchSummaryDTO = faCatchSummaryDTO;
    }

    public DefaultFeatureCollection getMovements() {
        return movements;
    }

    public void setMovements(DefaultFeatureCollection movements) {
        this.movements = movements;
    }

    public DefaultFeatureCollection getSegments() {
        return segments;
    }

    public void setSegments(DefaultFeatureCollection segments) {
        this.segments = segments;
    }

    public DefaultFeatureCollection getActivities() {
        return activities;
    }

    public void setActivities(DefaultFeatureCollection activities) {
        this.activities = activities;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}