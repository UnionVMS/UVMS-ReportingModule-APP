package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.vms.Report;
import eu.europa.ec.fisheries.uvms.reporting.security.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.LengthType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VelocityType;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.wsdl.user.types.Dataset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.reporting.service.util.Constants.*;

@Path("/report")
@Slf4j
public class ReportingResource extends UnionVMSResource {

    private static String APPLICATION_NAME ;
    public static final String USM_APPLICATION = "usmApplication";

    private @Context UriInfo context;
    private @EJB ReportServiceBean reportService;
    private @EJB VmsService vmsService;
    private @EJB USMService usmService;

    /**
     *
     * @responseMessage 200 Success
     * @responseMessage 500 Error
     *
     * @summary Gets a list of reports
     *
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listReports(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                @HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName,
                                @DefaultValue("Y") @QueryParam("existent") String existent) {

        final String username = request.getRemoteUser();
        log.info("{} is requesting listReports(...), with a scopeName={}", username, scopeName);

        try {
            Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
            if (username != null && features != null) {
                Collection<ReportDTO> reportsList;
                try {
                    reportsList = reportService.listByUsernameAndScope(features, username, scopeName, "Y".equals(existent));
                } catch (Exception e) {
                    log.error("Failed to list reports.", e);
                    return createErrorResponse();
                }

                return createSuccessResponse(reportsList);
            } else {
                return createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
            }
        } catch (ServiceException e) {
            return createErrorResponse("Unable to get user features from USM. Reason: " + e.getMessage());
        }
    }

    /**
     * lazy loading of the app name from the web.xml
     * @param request
     * @return
     */
    private String getApplicationName(HttpServletRequest request) {
        if (APPLICATION_NAME == null) {
            APPLICATION_NAME = request.getServletContext().getInitParameter(USM_APPLICATION);
        }
        return APPLICATION_NAME;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReport(@Context HttpServletRequest request, @Context HttpServletResponse response,
                              @PathParam("id") Long id, @HeaderParam("scopeName") String scopeName) {

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
            restResponse = createScNotFoundErrorResponse(ErrorCodes.ENTRY_NOT_FOUND);
        }

        return restResponse;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReport(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                 @PathParam("id") Long id, @HeaderParam("scopeName") String scopeName) {

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
    public Response updateReport(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                 ReportDTO report, @HeaderParam("scopeName") String scopeName, @PathParam("id") Long id) {

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
    public Response createReport(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                 ReportDTO report, @HeaderParam("scopeName") String scopeName) {

        Response result;
        String username = request.getRemoteUser();

        log.info("{} is requesting createReport(...), with a ID={}, scopeName: {}, visibility: {}", username, report.getId(), scopeName, report.getVisibility());

        if (StringUtils.isBlank(scopeName)) {
            result = createErrorResponse(ErrorCodes.USER_SCOPE_MISSING);
        } else {

            report.setCreatedBy(username);
            report.setScopeName(scopeName);

            ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToCreateReport(report, username);
            ReportDTO reportDTO;
            if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {
                try {
                    reportDTO = reportService.create(report);
                    result = createSuccessResponse(reportDTO.getId());
                } catch (Exception e) {
                    log.error("createReport failed.", e);
                    result = createErrorResponse(e.getMessage());
                }

            } else {
                result = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
            }
        }

        return result;
    }

    @PUT
    @Path("/share/{id}/{visibility}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response shareReport(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                @PathParam("id") Long id, @PathParam("visibility") String visibility,
                                @HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName) {

        String username = request.getRemoteUser();
        VisibilityEnum newVisibility = VisibilityEnum.getByName(visibility);
        Set<String> features;

        log.info("{} is requesting shareReport(...), with a ID={} with isShared={}", username, id, visibility);

        ReportDTO reportToUpdate;

        try {
            reportToUpdate = reportService.findById(id, username, scopeName);
        } catch (Exception e) {
            log.error("Sharing report failed.", e);
            return createErrorResponse(e.getMessage());
        }

        reportToUpdate.setVisibility(newVisibility);
        ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToShareReport(reportToUpdate, username);
        Response restResponse;

        if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {
            try {
                features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
                reportService.share(id, newVisibility, reportToUpdate.getCreatedBy(), reportToUpdate.getScopeName());
            } catch (Exception e) {
                log.error("Sharing report failed.", e);
                return createErrorResponse(e.getMessage());
            }
            restResponse = createSuccessResponse(AuthorizationCheckUtil.listAllowedVisibilitOptions(reportToUpdate.getCreatedBy(), username, features));
        } else {
            restResponse = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }

        return restResponse;
    }


    @POST
    @Path("/execute/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response runReport(@Context HttpServletRequest request, @Context HttpServletResponse response,
                              @PathParam("id") Long id, @HeaderParam("scopeName") String scopeName,
                              @HeaderParam("roleName") String roleName, DisplayFormat format) {

        String username = request.getRemoteUser();

        log.info("{} is requesting runReport(...), with a ID={}", username, id);

        try {
            Map additionalProperties = (Map) format.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);
            DateTime dateTime = DateUtils.UI_FORMATTER.parseDateTime((String) additionalProperties.get(TIMESTAMP));
            List<AreaIdentifierType> areaRestrictions = getRestrictionAreas(username, scopeName, roleName);
            ObjectNode jsonNodes = vmsService.getVmsDataByReportId(username, scopeName, id, areaRestrictions, dateTime).toJson(format);
            log.debug("Sending to Front-end the following JSON: {}", jsonNodes.toString());
            return createSuccessResponse(jsonNodes);

        } catch (Exception e) {
            log.error("Report execution failed.", e);
            return createErrorResponse(e.getMessage());
        }
    }

    @POST
    @Path("/execute/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response runReport(@Context HttpServletRequest request, Report report,
                              @HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName) {

        String username = request.getRemoteUser();
        log.info("{} is requesting runReport(...), with a report={}", username, report);

        try {
            Map additionalProperties = (Map) report.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);

            final VelocityType velocityType = 
                    VelocityType.valueOf(additionalProperties.get(SPEED_UNIT).toString().toUpperCase());
            final LengthType lengthType =
                    LengthType.valueOf(additionalProperties.get(DISTANCE_UNIT).toString().toUpperCase());

            final DisplayFormat displayFormat = new DisplayFormat(velocityType, lengthType);
            final List<AreaIdentifierType> areaRestrictions = getRestrictionAreas(username, scopeName, roleName);

            ObjectNode jsonNodes = vmsService.getVmsDataBy(report, areaRestrictions).toJson(displayFormat); // TODO Test this
            log.debug("Sending to Front-end the following JSON: {}", jsonNodes.toString());
            return createSuccessResponse(jsonNodes);

        } catch (Exception e) {
            log.error("Report execution failed.", e);
            return createErrorResponse(e.getMessage());
        }
    }

    private List<AreaIdentifierType> getRestrictionAreas(String username, String scopeName, String roleName) throws ServiceException {
        List<Dataset> datasets = usmService.getDatasetsPerCategory(USMSpatial.USM_DATASET_CATEGORY, username, USMSpatial.APPLICATION_NAME, roleName, scopeName);
        if (datasets != null && !datasets.isEmpty()) {
            List<AreaIdentifierType> areaRestrictions = new ArrayList<>(datasets.size());

            for (Dataset dataset : datasets) {
                int lastIndexDelimiter = dataset.getDiscriminator().lastIndexOf(USMSpatial.DELIMITER);

                if (lastIndexDelimiter > -1 )  {
                    AreaIdentifierType areaIdentifierType = new AreaIdentifierType();
                    //add AREATYPE/AREA_ID into a map
                    AreaType areaType = AreaType.valueOf(dataset.getDiscriminator().substring(0,lastIndexDelimiter));
                    String areaId = dataset.getDiscriminator().substring(lastIndexDelimiter + 1);

                    if (areaType!= null && StringUtils.isNotBlank(areaId)) {
                        areaIdentifierType.setAreaType(areaType);
                        areaIdentifierType.setId(areaId);
                        areaRestrictions.add(areaIdentifierType);
                    }
                }
            }

            return areaRestrictions;
        }
        return null;
    }


}
