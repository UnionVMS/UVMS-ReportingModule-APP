package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.apache.commons.collections4.CollectionUtils;
import org.geotools.feature.DefaultFeatureCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VmsDTO {

    private DefaultFeatureCollection movements = new DefaultFeatureCollection(null, MovementDTO.MOVEMENTYPE);
    private DefaultFeatureCollection segments = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);
    private List<TrackDTO> tracks = new ArrayList<>();
    private Map<String, Vessel> vesselMap;
    private Collection<MovementMapResponseType> movementMap;

    public VmsDTO(ImmutableMap<String, Vessel> vesselMap, Collection<MovementMapResponseType> movementMap) {
        this.vesselMap = vesselMap;
        this.movementMap = movementMap;
    }

    public ObjectNode toJson() throws ReportingServiceException {

        ObjectNode rootNode;

        try {

            if (CollectionUtils.isNotEmpty(movementMap)){
                for (MovementMapResponseType map : movementMap){
                    Vessel vessel = vesselMap.get(map.getKey());
                    if (vessel != null){
                        for (MovementType movement : map.getMovements()){
                            movements.add(new MovementDTO(movement, vessel).toFeature());
                        }
                        for (MovementSegment segment : map.getSegments()){
                            segments.add(new SegmentDTO(segment, vessel).toFeature());
                        }
                        for (MovementTrack track : map.getTracks()){
                            tracks.add(new TrackDTO(track, vessel));
                        }
                    }
                }
            }

            ObjectMapper mapper = new ObjectMapper();


            ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(movements));

            ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(segments));

            mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            rootNode = mapper.createObjectNode();
            rootNode.set("movements", movementsNode);
            rootNode.set("segments", segmentsNode);
            rootNode.putPOJO("tracks", tracks);

        } catch (IOException e) {
            throw new ReportingServiceException("ERROR");
        }

        return rootNode;
    }
}
