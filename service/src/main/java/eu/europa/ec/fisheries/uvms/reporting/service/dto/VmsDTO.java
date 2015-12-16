package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.apache.commons.collections4.CollectionUtils;
import org.geotools.feature.DefaultFeatureCollection;

import javax.measure.converter.UnitConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VmsDTO {

    private DefaultFeatureCollection movements = new DefaultFeatureCollection(null, MovementDTO.SIMPLE_FEATURE_TYPE);
    private DefaultFeatureCollection segments = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);
    private List<TrackDTO> tracks = new ArrayList<>();
    private Map<String, Vessel> vesselMap;
    private Collection<MovementMapResponseType> movementMap;

    public VmsDTO(Map<String, Vessel> vesselMap, Collection<MovementMapResponseType> movementMap) {
        this.vesselMap = vesselMap;
        this.movementMap = movementMap;
    }

    public ObjectNode toJson(DisplayFormat format) throws ReportingServiceException {

        ObjectNode rootNode;

        try {

            if (CollectionUtils.isNotEmpty(movementMap)){
                for (MovementMapResponseType map : movementMap){
                    Vessel vessel = vesselMap.get(map.getKey());
                    if (vessel != null){
                        for (MovementType movement : map.getMovements()){
                            movements.add(new MovementDTO(movement, vessel, format).toFeature());
                        }
                        for (MovementSegment segment : map.getSegments()){
                            segments.add(new SegmentDTO(segment, vessel, format).toFeature());
                        }
                        for (MovementTrack track : map.getTracks()){
                            tracks.add(new TrackDTO(track, vessel, format));
                        }
                    }
                }
            }

            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            rootNode = mapper.createObjectNode();
            rootNode.set("movements", new FeatureToGeoJsonJacksonMapper().convert(movements));
            rootNode.set("segments", new FeatureToGeoJsonJacksonMapper().convert(segments));
            rootNode.putPOJO("tracks", tracks);

        } catch (ParseException | IOException e) {
            throw new ReportingServiceException("ERROR WHILE CREATING GEOJSON", e);
        }

        return rootNode;
    }

}
