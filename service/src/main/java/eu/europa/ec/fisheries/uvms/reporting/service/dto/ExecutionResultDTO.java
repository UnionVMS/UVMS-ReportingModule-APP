/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.activity.model.dto.facatch.FACatchSummaryDTO;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FACatchSummaryRecord;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import org.apache.commons.collections4.CollectionUtils;
import org.geotools.feature.DefaultFeatureCollection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExecutionResultDTO {

    private DefaultFeatureCollection movements = new DefaultFeatureCollection(null, MovementDTO.SIMPLE_FEATURE_TYPE);
    private DefaultFeatureCollection segments = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);
    private DefaultFeatureCollection activities = new DefaultFeatureCollection(null, ActivityDTO.ACTIVITY);
    private List<TrackDTO> tracks = new ArrayList<>();
    private Map<String, Asset> assetMap;
    private Collection<MovementMapResponseType> movementMap;
    private List<TripDTO> trips;
    private List<FishingActivitySummary> activityList;
    private FACatchSummaryDTO faCatchSummaryDTO;

    public ObjectNode toJson(DisplayFormat format) throws ReportingServiceException {

        ObjectNode rootNode;

        try {

            if (CollectionUtils.isNotEmpty(movementMap)){
                for (MovementMapResponseType map : movementMap){
                    Asset asset = assetMap.get(map.getKey());
                    if (asset != null){
                        for (MovementType movement : map.getMovements()){
                            movements.add(new MovementDTO(movement, asset, format).toFeature());
                        }
                        for (MovementSegment segment : map.getSegments()){
                            segments.add(new SegmentDTO(segment, asset, format).toFeature());
                        }
                        for (MovementTrack track : map.getTracks()){
                            tracks.add(new TrackDTO(track, asset, format));
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(activityList)) {
                for (FishingActivitySummary summary : activityList) {
                    activities.add(new ActivityDTO(summary).toFeature());
                }
            }

            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            rootNode = mapper.createObjectNode();
            rootNode.set("movements", new FeatureToGeoJsonJacksonMapper().convert(movements));
            rootNode.set("segments", new FeatureToGeoJsonJacksonMapper().convert(segments));
            rootNode.putPOJO("tracks", tracks);
            rootNode.putPOJO("trips", trips);
            rootNode.putPOJO("activities", new FeatureToGeoJsonJacksonMapper().convert(activities));
            rootNode.putPOJO("criteria", faCatchSummaryDTO);

        } catch (ParseException | IOException e) {
            throw new ReportingServiceException("ERROR WHILE CREATING GEOJSON", e);
        }

        return rootNode;
    }

    public Map<String, Asset> getAssetMap() {
        return assetMap;
    }

    public void setAssetMap(Map<String, Asset> assetMap) {
        this.assetMap = assetMap;
    }

    public Collection<MovementMapResponseType> getMovementMap() {
        return movementMap;
    }

    public void setMovementMap(Collection<MovementMapResponseType> movementMap) {
        this.movementMap = movementMap;
    }

    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }

    public List<FishingActivitySummary> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<FishingActivitySummary> activityList) {
        this.activityList = activityList;
    }

    public FACatchSummaryDTO getFaCatchSummaryDTO() {
        return faCatchSummaryDTO;
    }

    public void setFaCatchSummaryDTO(FACatchSummaryDTO faCatchSummaryDTO) {
        this.faCatchSummaryDTO = faCatchSummaryDTO;
    }
}