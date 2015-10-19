package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.security.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.Set;

@Path("/report")
public class ReportingResource extends UnionVMSResource {

    final static Logger LOG = LoggerFactory.getLogger(ReportingResource.class);

    @Context
    private UriInfo context;

    @EJB
    private ReportServiceBean reportService;

    @EJB
    private VmsService vmsService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listReports(@Context HttpServletRequest request,
                                @Context HttpServletResponse response,
                                @HeaderParam("scopeName") String scopeName) {
        final String username = request.getRemoteUser();

        LOG.info("{} is requesting listReports(...), with a scopeName={}", username, scopeName);

        Object featuresObj =  request.getServletContext().getAttribute(AuthConstants.HTTP_SERVLET_CONTEXT_ATTR_FEATURES);

        if (featuresObj != null) {

            Set<String> features = (Set<String>) featuresObj;

            Collection < ReportDTO > reportsList;
            try {
                reportsList = reportService.listByUsernameAndScope(features, username, scopeName);
            } catch (ReportingServiceException e) {
                LOG.error("Failed to list reports.", e);
                return createErrorResponse();
            }

            for (ReportDTO report : reportsList) {
                report.setExecutionLogs(report.filterLogsByUser(username));
            }

            return createSuccessResponse(reportsList);
        } else {
            return createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReport(@Context HttpServletRequest request,
                              @Context HttpServletResponse response,
                              @PathParam("id") Long id,
                              @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        LOG.info("{} is requesting getReport(...), with an ID={}", username, id );

        ReportDTO report;
        try {
            report = reportService.findById(id, username, scopeName);
        } catch (ReportingServiceException e) {
            LOG.error("Failed to get report.", e);
            return createErrorResponse();
        }

        Response restResponse;

        if (report != null) {
            restResponse = createSuccessResponse(report);
        } else {
            restResponse = createErrorResponse(ErrorCodes.ENTRY_NOT_FOUND);
        }

        return restResponse;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReport(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 @PathParam("id") Long id,
                                 @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        LOG.info("{} is requesting deleteReport(...), with a ID={} and scopeName={}", username, id, scopeName);
        ReportDTO originalReport;

        try {
            originalReport = reportService.findById(id, username, scopeName); //we need the original report because of the 'owner/createdBy' attribute, which is not contained in the JSON
        } catch (ReportingServiceException e) {
            LOG.error("Failed to get report.", e);
            return createErrorResponse();
        }

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(originalReport, username);

        if ( requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
            createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }

        try {
            reportService.delete(id, username, scopeName);
        } catch (ReportingServiceException exc) {
            LOG.error("Report deletion failed." , exc);
            return  createErrorResponse(ErrorCodes.DELETE_FAILED);
        }

        return createSuccessResponse();
    }


    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReport(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 ReportDTO report,
                                 @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        LOG.info("{} is requesting updateReport(...), with a ID={}", username, report.getId());
        ReportDTO originalReport;

        try {
            originalReport = reportService.findById(report.getId(), username, scopeName); //we need the original report because of the 'owner/createdBy' attribute, which is not contained in the JSON
        } catch (ReportingServiceException e) {
            LOG.error("Failed to get report.", e);
            return createErrorResponse();
        }

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToEditReport(originalReport, username);

        if ( requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
            createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }

        boolean isUpdate = false;

        try {
            isUpdate = reportService.update(report);
        } catch (ReportingServiceException exc) {
            LOG.error("Update failed.", exc);
        }

        Response restResponse;

        if (isUpdate){
            restResponse = createSuccessResponse();
        } else {
            restResponse = createErrorResponse(ErrorCodes.UPDATE_FAILED);
        }
        return restResponse;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReport(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 ReportDTO report,
                                 @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        LOG.info("{} is requesting createReport(...), with a ID={}", username, report.getId());

        report.setCreatedBy(username);
        report.setScopeName(scopeName);

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToCreateReport(report, username);

        if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {
            try {
                reportService.create(report);
            } catch (ReportingServiceException e) {
                LOG.error("createReport failed.", e);
                return createErrorResponse();
            }
            return createSuccessResponse();
        } else {
            return createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }

    }

    @PUT
    @Path("/share/{id}/{visibility}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareReport(@Context HttpServletRequest request,
                                @Context HttpServletResponse response,
                                @PathParam("id") Long id,
                                @PathParam("visibility") String visibility,
                                @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        LOG.info("{} is requesting shareReport(...), with a ID={} with isShared={}", username, id,visibility);

        ReportDTO reportToUpdate;

        try {
            reportToUpdate = reportService.findById(id, username, scopeName);
        } catch (ReportingServiceException e) {
            LOG.error("Sharing report failed.", e);
            return createErrorResponse();
        }

        reportToUpdate.setVisibility(VisibilityEnum.valueOf(visibility));

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToShareReport(reportToUpdate, username);

        Response restResponse;

        if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {
            try {
                reportService.update(reportToUpdate);
            } catch (ReportingServiceException e) {
                LOG.error("Sharing report failed.", e);
                return createErrorResponse();
            }
            restResponse = createSuccessResponse();
        } else {
            restResponse = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }

        return restResponse;
    }


    @GET
    @Path("/execute/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response runReport(@Context HttpServletRequest request,
                              @Context HttpServletResponse response,
                              @PathParam("id") Long id,
                              @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        LOG.info("{} is requesting shareReport(...), with a ID={}",username, id);

        VmsDTO vmsDto = null;
        ObjectNode jsonNodes;

        try {
            vmsDto = vmsService.getVmsDataByReportId(username, scopeName, id);
            jsonNodes = vmsDto.toObjectNode();
            return createSuccessResponse(jsonNodes);

        } catch (ReportingServiceException e) {
            LOG.error("Report execution failed.", e);
            return createErrorResponse();
        }

    }

}
