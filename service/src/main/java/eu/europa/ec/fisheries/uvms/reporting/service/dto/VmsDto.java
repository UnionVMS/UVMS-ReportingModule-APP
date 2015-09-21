package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.geotools.feature.DefaultFeatureCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VmsDto {

    private DefaultFeatureCollection movements;
    private DefaultFeatureCollection segments;
    private List<TrackDto> tracks;

    public VmsDto(DefaultFeatureCollection movements, DefaultFeatureCollection segments, List<TrackDto> tracks) {
        this.segments = segments;
        this.movements = movements;
        this.tracks = tracks;
    }

    public List<TrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDto> tracks) {
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

    public static VmsDto getVmsDto(Map<String, Vessel> vesselMapByGuid, List<MovementMapResponseType> mapResponseTypes) {
        List<TrackDto> tracks = new ArrayList<>();
        DefaultFeatureCollection movementFeatureCollection = new DefaultFeatureCollection(null, MovementDto.MOVEMENT);
        DefaultFeatureCollection segmentsFeatureCollection = new DefaultFeatureCollection(null, SegmentDto.SEGMENT);

        for (MovementMapResponseType map : mapResponseTypes){
            Vessel vessel = vesselMapByGuid.get(map.getKey());
            if (vessel != null){
                for (MovementType movement : map.getMovements()){
                    movementFeatureCollection.add(new MovementDto(movement, vessel).toFeature());
                }
                for (MovementSegment segment : map.getSegments()){
                    segmentsFeatureCollection.add(new SegmentDto(segment, vessel).toFeature());
                }
                for (MovementTrack track : map.getTracks()){
                    tracks.add(new TrackDto(track, vessel));
                }
            }
        }

        return new VmsDto(movementFeatureCollection, segmentsFeatureCollection, tracks);
    }
}
