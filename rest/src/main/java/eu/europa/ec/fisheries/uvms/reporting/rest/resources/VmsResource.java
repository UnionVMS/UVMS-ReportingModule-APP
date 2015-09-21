package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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

    @Inject
    private ReportingResource reportingResource;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{id}")
    @SuppressWarnings("unchecked")
    public ResponseDto getVmsData(@Context HttpServletRequest request,
                                  @Context HttpServletResponse response, @PathParam("id") Long id) throws SchemaException, IOException {

        VmsDto vmsDto = vmsService.getVmsDataByReportId(id);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getMovements()));
        ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getSegments()));

        rootNode.set("movements", movementsNode);
        rootNode.set("segments", segmentsNode);
        rootNode.set("tracks", mapper.readTree(objectMapper.writeValueAsString(vmsDto.getTracks())));

        reportingResource.runReport(request, response, id);
        return new ResponseDto(rootNode, HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/mock/{id}")
    @SuppressWarnings("unchecked")
    public ResponseDto getVmsMockData(@Context HttpServletRequest request,
                                  @Context HttpServletResponse response, @PathParam("id") Long id) throws SchemaException, IOException {

        VmsDto vmsDto = vmsService.getVmsMockData(id);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getMovements()));
        ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getSegments()));

        rootNode.set("movements", movementsNode);
        rootNode.set("segments", segmentsNode);
        rootNode.set("tracks", mapper.readTree(objectMapper.writeValueAsString(vmsDto.getTracks())));

        reportingResource.runReport(request, response, id);
        return new ResponseDto(rootNode, HttpServletResponse.SC_OK);
    }
}
