package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.security.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.utils.SecuritySessionUtils;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Length;
import static javax.measure.unit.NonSI.*;
import static javax.measure.unit.SI.*;

@Path("/report")
@Slf4j
public class ReportingResource extends UnionVMSResource {

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

        log.info("{} is requesting listReports(...), with a scopeName={}", username, scopeName);

        Set<String> features = getCachedUserFeatures(request);

        if (username != null && features != null) {

            Collection<ReportDTO> reportsList;
            try {
                reportsList = reportService.listByUsernameAndScope(features, username, scopeName);
            } catch (Exception e) {
                log.error("Failed to list reports.", e);
                return createErrorResponse();
            }

            return createSuccessResponse(reportsList);
        } else {
            return createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
    }

    // UT
    protected Set<String> getCachedUserFeatures(HttpServletRequest request) {
        return SecuritySessionUtils.getCachedUserFeatures(request.getSession());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReport(@Context HttpServletRequest request,
                              @Context HttpServletResponse response,
                              @PathParam("id") Long id,
                              @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        log.info("{} is requesting getReport(...), with an ID={}", username, id);

        ReportDTO report;
        try {
            report = reportService.findById(id, username, scopeName);
        } catch (Exception e) {
            log.error("Failed to get report.", e);
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

        log.info("{} is requesting deleteReport(...), with a ID={} and scopeName={}", username, id, scopeName);
        ReportDTO originalReport;

        try {
            originalReport = reportService.findById(id, username, scopeName); //we need the original report because of the 'owner/createdBy' attribute, which is not contained in the JSON
        } catch (Exception e) {
            log.error("Failed to get report.", e);
            return createErrorResponse();
        }

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(originalReport, username);

        if (requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
            createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }

        try {
            reportService.delete(id, username, scopeName);
        } catch (Exception exc) {
            log.error("Report deletion failed.", exc);
            return createErrorResponse(ErrorCodes.DELETE_FAILED);
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

        log.info("{} is requesting updateReport(...), with a ID={}", username, report.getId());
        ReportDTO originalReport;

        try {
            originalReport = reportService.findById(report.getId(), username, scopeName); //we need the original report because of the 'owner/createdBy' attribute, which is not contained in the JSON
        } catch (Exception e) {
            log.error("Failed to get report.", e);
            return createErrorResponse();
        }

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToEditReport(originalReport, username);

        if (requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {

            createErrorResponse(ErrorCodes.NOT_AUTHORIZED);

        }

        try {
            reportService.update(report, originalReport.getWithMap(), originalReport.getMapConfiguration());
        } catch (Exception exc) {
            log.error("Update failed.", exc);
            return createErrorResponse(ErrorCodes.UPDATE_FAILED);
        }

        return createSuccessResponse();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReport(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 ReportDTO report,
                                 @HeaderParam("scopeName") String scopeName) {

        String username = request.getRemoteUser();

        log.info("{} is requesting createReport(...), with a ID={}", username, report.getId());

        report.setCreatedBy(username);
        report.setScopeName(scopeName);

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToCreateReport(report, username);

        if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {
            try {
                reportService.create(report);
            } catch (Exception e) {
                log.error("createReport failed.", e);
                return createErrorResponse(e.getMessage());
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

        log.info("{} is requesting shareReport(...), with a ID={} with isShared={}", username, id, visibility);

        ReportDTO reportToUpdate;

        try {

            reportToUpdate = reportService.findById(id, username, scopeName);

        } catch (Exception e) {

            log.error("Sharing report failed.", e);

            return createErrorResponse(e.getMessage());

        }

        reportToUpdate.setVisibility(VisibilityEnum.valueOf(visibility));

        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToShareReport(reportToUpdate, username);

        Response restResponse;

        if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {

            try {

                reportService.share(reportToUpdate, reportToUpdate.getWithMap(), reportToUpdate.getMapConfiguration());

            }

            catch (Exception e) {

                log.error("Sharing report failed.", e);

                return createErrorResponse(e.getMessage());
            }

            restResponse = createSuccessResponse();

        }

        else {

            restResponse = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);

        }

        return restResponse;
    }


    @POST
    @Path("/execute/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response runReport(@Context HttpServletRequest request,
                              @Context HttpServletResponse response,
                              @PathParam("id") Long id,
                              @HeaderParam("scopeName") String scopeName,
                              DisplayFormat format) {

        String username = request.getRemoteUser();

        log.info("{} is requesting runReport(...), with a ID={}", username, id);

        VmsDTO vmsDto;
        ObjectNode jsonNodes;

        try {

            vmsDto = vmsService.getVmsDataByReportId(username, scopeName, id);

            jsonNodes = vmsDto.toJson(format);
            log.debug("Sending to Front-end the following JSON: {}", jsonNodes.toString());
            return createSuccessResponse(jsonNodes);

        } catch (Exception e) {
            log.error("Report execution failed.", e);
            return createErrorResponse(e.getMessage());
        }
    }

}
