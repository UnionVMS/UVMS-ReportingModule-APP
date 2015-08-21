package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJson;
import lombok.extern.slf4j.Slf4j;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.type.AttributeDescriptor;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public ResponseDto getMockData() throws SchemaException, IOException {

        log.info("TETETETEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_TIME_UI_FORMAT);

        Set<Integer> vesselIds = new HashSet<>();
        vesselIds.add(1);vesselIds.add(2);
        VmsDto monitoringMockData = vmsService.getMonitoringMockData(vesselIds);
        DefaultFeatureCollection movementFeatureCollection = new DefaultFeatureCollection(null, VmsDto.MOVEMENT_TYPE);
        Map<AssetDto, List<MovementDto>> monitoringDtoMovements = monitoringMockData.getMovements();

        // movements
        for (Map.Entry<AssetDto, List<MovementDto>> entry : monitoringDtoMovements.entrySet()) {

            AssetDto assetDto = entry.getKey();
            List<MovementDto> movementDtos = entry.getValue();
            for(int i = 0; i < movementDtos.size(); i++){
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(VmsDto.MOVEMENT_TYPE);
                featureBuilder.set("geometry", movementDtos.get(i).getGeometry());
                addVesselFeatures(featureBuilder, assetDto);
                featureBuilder.set(VmsDto.POSITION_TIME, formatter.format(movementDtos.get(i).getPositionTime()));
                featureBuilder.set(VmsDto.MEASURED_SPEED, movementDtos.get(i).getMeasuredSpeed());
                featureBuilder.set(VmsDto.CONNECT_ID, movementDtos.get(i).getConnectId());
                featureBuilder.set(VmsDto.STATUS, movementDtos.get(i).getStatus());
                featureBuilder.set(VmsDto.COURSE, movementDtos.get(i).getCourse());
                featureBuilder.set(VmsDto.MESSAGE_TYPE, movementDtos.get(i).getMessageType().value());
                featureBuilder.set(VmsDto.CALCULATED_SPEED, movementDtos.get(i).getCalculatedSpeed());
                movementFeatureCollection.add(featureBuilder.buildFeature(String.valueOf(i)));
            }
        }

        // segments
        DefaultFeatureCollection segmentsFeatureCollection = new DefaultFeatureCollection(null, VmsDto.SEGMENT_TYPE);
        Map<AssetDto, List<SegmentDto>> monitoringDtoSegments = monitoringMockData.getSegments();

        for (Map.Entry<AssetDto, List<SegmentDto>> entry : monitoringDtoSegments.entrySet()) {
            AssetDto assetDto = entry.getKey();
            List<SegmentDto> segmentDtos = entry.getValue();
            for(int i = 0; i < segmentDtos.size(); i++){
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(VmsDto.SEGMENT_TYPE);
                addVesselFeatures(featureBuilder, assetDto);
                featureBuilder.set("geometry", segmentDtos.get(i).getGeometry());
                featureBuilder.set(VmsDto.AVERAGE_SPEED, segmentDtos.get(i).getAverageSpeed());
                featureBuilder.set(VmsDto.AVERAGE_COURSE, segmentDtos.get(i).getAverageCourse());
                segmentsFeatureCollection.add(featureBuilder.buildFeature(String.valueOf(i)));
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        JsonNode movementsNode = objectMapper.readTree(new FeatureToGeoJson().convert(movementFeatureCollection));
        JsonNode segmentsNode = objectMapper.readTree(new FeatureToGeoJson().convert(segmentsFeatureCollection));

        rootNode.set("movements", movementsNode);
        rootNode.set("segments", segmentsNode);
        rootNode.set("tracks", null);

        return new ResponseDto(rootNode, ResponseCode.OK);
    }

    private void addVesselFeatures(SimpleFeatureBuilder featureBuilder, AssetDto assetDto) {
        featureBuilder.set(VmsDto.CFR, assetDto.getCfr());
        featureBuilder.set(VmsDto.IRCS, assetDto.getIrcs());
        featureBuilder.set(VmsDto.VESSEL_ID, assetDto.getVesselId().getValue());
        featureBuilder.set(VmsDto.COUNTRY, assetDto.getCountryCode());
    }
}
