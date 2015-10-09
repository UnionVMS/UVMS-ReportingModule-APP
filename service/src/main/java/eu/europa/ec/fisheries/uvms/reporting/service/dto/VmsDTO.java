package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.Builder;
import org.geotools.feature.DefaultFeatureCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VmsDTO {

    private DefaultFeatureCollection movements;
    private DefaultFeatureCollection segments;
    private List<TrackDTO> tracks;

    @Builder
    public VmsDTO(DefaultFeatureCollection movements, DefaultFeatureCollection segments, List<TrackDTO> tracks) {
        this.segments = segments;
        this.movements = movements;
        this.tracks = tracks;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDTO> tracks) {
        this.tracks = tracks;
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

    public static VmsDTO getVmsDto(Map<String, Vessel> vesselMapByGuid, List<MovementMapResponseType> mapResponseTypes) {
        List<TrackDTO> tracks = new ArrayList<>();
        DefaultFeatureCollection movementFeatureCollection = new DefaultFeatureCollection(null, MovementDTO.MOVEMENT);
        DefaultFeatureCollection segmentsFeatureCollection = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);

        for (MovementMapResponseType map : mapResponseTypes){
            Vessel vessel = vesselMapByGuid.get(map.getKey());
            if (vessel != null){
                for (MovementType movement : map.getMovements()){
                    movementFeatureCollection.add(new MovementDTO(movement, vessel).toFeature());
                }
                for (MovementSegment segment : map.getSegments()){
                    segmentsFeatureCollection.add(new SegmentDTO(segment, vessel).toFeature());
                }
                for (MovementTrack track : map.getTracks()){
                    tracks.add(new TrackDTO(track, vessel));
                }
            }
        }

        return new VmsDTO(movementFeatureCollection, segmentsFeatureCollection, tracks);
    }

    public ObjectNode toObjectNode() {

        ObjectNode rootNode = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.createObjectNode();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(getMovements()));
            ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(getSegments()));

            rootNode.set("movements", movementsNode);
            rootNode.set("segments", segmentsNode);

            rootNode.set("tracks", mapper.readTree(objectMapper.writeValueAsString(getTracks())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootNode;
    }
}
