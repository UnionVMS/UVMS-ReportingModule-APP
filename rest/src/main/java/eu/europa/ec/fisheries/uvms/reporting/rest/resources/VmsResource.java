package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;

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
                                  @Context HttpServletResponse response, @PathParam("id") Long id) {

        VmsDTO vmsDto = null;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        try {
            vmsDto = vmsService.getVmsDataByReportId(id);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getMovements()));
            ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getSegments()));

            rootNode.set("movements", movementsNode);
            rootNode.set("segments", segmentsNode);
            rootNode.set("tracks", mapper.readTree(objectMapper.writeValueAsString(vmsDto.getTracks())));

            reportingResource.runReport(request, response, id);

        } catch (ServiceException | IOException e) {
            e.printStackTrace();
        }
        return new ResponseDto(rootNode, HttpServletResponse.SC_OK);
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/mock/{id}")
    @SuppressWarnings("unchecked")
    public ResponseDto getVmsMockData(@Context HttpServletRequest request,
                                      @Context HttpServletResponse response, @PathParam("id") Long id) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        try {

            VmsDTO vmsDto = vmsService.getVmsMockData(id);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode movementsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getMovements()));
            ObjectNode segmentsNode = (ObjectNode) mapper.readTree(new FeatureToGeoJsonMapper().convert(vmsDto.getSegments()));

            rootNode.set("movements", movementsNode);
            rootNode.set("segments", segmentsNode);
            rootNode.set("tracks", mapper.readTree(objectMapper.writeValueAsString(vmsDto.getTracks())));

            reportingResource.runReport(request, response, id);

        } catch (ServiceException | IOException e) {
            e.printStackTrace();
        }
        return new ResponseDto(rootNode, HttpServletResponse.SC_OK);
    }
}
