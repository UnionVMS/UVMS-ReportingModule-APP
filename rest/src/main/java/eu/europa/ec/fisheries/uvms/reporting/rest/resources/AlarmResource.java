package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AlarmServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.rules.AlarmMovementList;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by padhyad on 3/25/2016.
 */
@Path("/alarms")
@Slf4j
public class AlarmResource extends UnionVMSResource {

    @EJB
    private AlarmServiceBean alarmService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAlarms(AlarmMovementList alarmMovementList, @Context HttpServletRequest request) {
        try {
            return createSuccessResponse(alarmService.getAlarmsForMovements(alarmMovementList, request.getRemoteUser()));
        } catch (ReportingServiceException e) {
            return createErrorResponse(e.getMessage());
        }
    }
}
