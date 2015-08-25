package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * //TODO create test
 */
@Path("/vms")
@Slf4j
public class VmsResource {

    @EJB
    private VmsService vmsService;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/mock")
    @SuppressWarnings("unchecked")
    public ResponseDto getVmsData() throws SchemaException, IOException {

        log.info("TEST");

        Set<Integer> vesselIds = new HashSet<>();
        vesselIds.add(1);vesselIds.add(2);
        VmsDto vmsDto = vmsService.getVmsMockData(vesselIds);
        DefaultFeatureCollection movementFeatureCollection = new DefaultFeatureCollection(null, MovementDto.MOVEMENT);
        for (MovementDto movementDto : vmsDto.getMovements()) {
            movementFeatureCollection.add(movementDto.toFeature());
        }

        DefaultFeatureCollection segmentsFeatureCollection = new DefaultFeatureCollection(null, SegmentDto.SEGMENT);
        for (SegmentDto segmentDto : vmsDto.getSegments()) {
            segmentsFeatureCollection.add(segmentDto.toFeature());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(movementFeatureCollection));
        ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(segmentsFeatureCollection));

        rootNode.set("movements", movementsNode);
        rootNode.set("segments", segmentsNode);
        rootNode.set("tracks", null);

        return new ResponseDto(rootNode, ResponseCode.OK);
    }
}
