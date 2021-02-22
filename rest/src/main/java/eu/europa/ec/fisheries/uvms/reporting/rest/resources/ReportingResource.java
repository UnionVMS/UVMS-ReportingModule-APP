/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.ADDITIONAL_PROPERTIES;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.DISTANCE_UNIT;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.SPEED_UNIT;
import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.TIMESTAMP;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityFeaturesEnum;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.rest.utils.ReportingExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportExecutionService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.impl.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CriteriaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.LengthType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.Projection;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.VelocityType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportResultMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ServiceLayerUtils;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.wsdl.user.types.Dataset;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

@Path("/report")
@Slf4j
@NoArgsConstructor
public class ReportingResource extends UnionVMSResource {

	private static final String DEFAULT_REPORT_ID = "DEFAULT_REPORT_ID";
	private static final String USM_APPLICATION = "usmApplication";

	@HeaderParam("authorization")
    private String authorization;

    @HeaderParam("scopeName")
    private String scopeName;

    @HeaderParam("roleName")
    private String roleName;

    @Context
    private HttpServletRequest servletRequest;

	@EJB
	private ReportServiceBean reportService;

	@EJB
	private ReportExecutionService reportExecutionService;

	@Inject
	private ReportResultMapper reportResultMapper;

	@EJB
	private USMService usmService;

	private String applicationName;

	/**
	 * @responseMessage 200 Success
	 * @responseMessage 500 Error
	 * @summary Gets a list of reports
	 */
	@GET
	@Path("/list")
	@Produces(APPLICATION_JSON)
	@Interceptors(ReportingExceptionInterceptor.class)
	public Response listReports(@Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName,
			@HeaderParam("roleName") String roleName, @DefaultValue("Y") @QueryParam("existent") String existent)
			throws ServiceException, ReportingServiceException {
		Collection<ReportDTO> reportDTOs = listReportByUsernameAndScope(request, scopeName, roleName, existent, null);
		return createSuccessResponse(reportDTOs);
	}

	@GET
	@Path("/list/lastexecuted/{numberOfReport}")
	@Produces(APPLICATION_JSON)
	@Interceptors(ReportingExceptionInterceptor.class)
	public Response listLastExecutedReports(@PathParam("numberOfReport") Integer numberOfReport,
			@DefaultValue("Y") @QueryParam("existent") String existent)
			throws ServiceException, ReportingServiceException {
		if (numberOfReport == null || numberOfReport == 0) {
			return createErrorResponse("Number of last executed report cannot be null or 0");
		}
		Collection<ReportDTO> reportDTOs = listReportByUsernameAndScope(servletRequest, scopeName, roleName, existent,
				numberOfReport);
		return createSuccessResponse(reportDTOs);
	}

	private Collection<ReportDTO> listReportByUsernameAndScope(HttpServletRequest request, String scopeName,
			String roleName, String existent, Integer numberOfReport)
			throws ServiceException, ReportingServiceException {
		final String username = request.getRemoteUser();
		log.debug("{} is requesting listReports(...), with a scopeName={}", username, scopeName);
		Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
		String defaultId = usmService.getUserPreference(DEFAULT_REPORT_ID, username, getApplicationName(request),
				roleName, scopeName);
		Long defaultReportId = StringUtils.isNotBlank(defaultId) ? Long.valueOf(defaultId) : null;
		ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToListReports();

		if (username != null && features != null
				&& (requiredFeature == null || request.isUserInRole(requiredFeature.toString()))) {
			return reportService.listByUsernameAndScope(features, username, scopeName, "Y".equals(existent),
					defaultReportId, numberOfReport);
		} else {
			throw new ReportingServiceException(ErrorCodes.NOT_AUTHORIZED);
		}
	}

	/**
	 * lazy loading of the app name from the web.xml
	 *
	 * @param request
	 * @return
	 */
	private String getApplicationName(HttpServletRequest request) {
		if (applicationName == null) {
			applicationName = request.getServletContext().getInitParameter(USM_APPLICATION);
		}
		return applicationName;
	}

	@GET
	@Path("/{id}")
	@Produces(APPLICATION_JSON)
	public Response getReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName) {

		String username = request.getRemoteUser();
		ReportDTO report;

		try {
			boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());
			Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName,
					scopeName);
			List<String> permittedServiceLayers = new ArrayList<>(ServiceLayerUtils
					.getUserPermittedLayersNames(usmService, request.getRemoteUser(), roleName, scopeName));
			report = reportService.findById(features, id, username, scopeName, isAdmin, permittedServiceLayers);
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
	@Produces(APPLICATION_JSON)
	public Response deleteReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName) {

		String username = request.getRemoteUser();

		log.debug("{} is requesting deleteReport(...), with a ID={} and scopeName={}", username, id, scopeName);
		ReportDTO originalReport = null;
		boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());

		Response response = createSuccessResponse();
		try {
			Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
			originalReport = reportService.findById(features, id, username, scopeName, isAdmin, null);
		} catch (Exception e) {
			String errorMsg = "Failed to get report.";
			log.error(errorMsg, e);
			response = createErrorResponse(errorMsg);
		}

		if (originalReport == null) {
            response = createScNotFoundErrorResponse(ErrorCodes.ENTRY_NOT_FOUND);
		}

		ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(originalReport,
				username);

		if (requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
			response = createScNotFoundErrorResponse(ErrorCodes.NOT_AUTHORIZED);
		}

		try {
			reportService.delete(id, username, scopeName, isAdmin);
		} catch (Exception exc) {
			log.error("Report deletion failed.", exc);
			response = createErrorResponse(ErrorCodes.DELETE_FAILED);
		}

		return response;
	}

	@PUT
	@Path("/{id}")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response updateReport(@Context HttpServletRequest request, ReportDTO report,
			@DefaultValue("default") @QueryParam(value = "projection") String projection,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName,
			@PathParam("id") Long id) {

		String username = request.getRemoteUser();
		log.info("{} is requesting updateReport(...), with a ID={}", username, report.getId());
		Response result;

		try {
			Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName,
					scopeName);
			boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());
			List<String> permittedServiceLayers = new ArrayList<>(ServiceLayerUtils
					.getUserPermittedLayersNames(usmService, request.getRemoteUser(), roleName, scopeName));
			ReportDTO originalReport = reportService.findById(features, report.getId(), username, scopeName, isAdmin,
					permittedServiceLayers); // we need the original report because of the 'owner/createdBy' attribute,
												// which is not contained in the JSO
			ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToEditReport(originalReport,
					username);

			if (requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
				result = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
			} else {
				ReportDTO update = reportService.update(report, username, originalReport.getWithMap(),
						originalReport.getMapConfiguration());
				switch (Projection.valueOf(projection.toUpperCase())) {

				case DETAILED:
					result = createSuccessResponse(update);
					break;

				default:
					result = createSuccessResponse(update.getId());
				}
			}

		} catch (Exception exc) {
			log.error("Update failed.", exc);
			result = createErrorResponse(ErrorCodes.UPDATE_FAILED);
		}
		return result;
	}

	@POST
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response createReport(@Context HttpServletRequest request, ReportDTO report,
			@DefaultValue("default") @QueryParam(value = "projection") String projection,
			@HeaderParam("scopeName") String scopeName) {
		Response result;
		String username = request.getRemoteUser();

		log.debug("{} is requesting createReport(...), with a ID={}, scopeName: {}, visibility: {}", username,
				report.getId(), scopeName, report.getVisibility());

		if (StringUtils.isBlank(scopeName)) {
			result = createErrorResponse(ErrorCodes.USER_SCOPE_MISSING);
		} else {
			if (isScopeAllowed(report.getVisibility(), request)) {
				report.setCreatedBy(username);
				report.setScopeName(scopeName);

				ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToCreateReport(report,
						username);
				ReportDTO reportDTO;
				if (requiredFeature == null || request.isUserInRole(requiredFeature.toString())) {
					try {
						reportDTO = reportService.create(report, username);
						switch (Projection.valueOf(projection.toUpperCase())) {

						case DETAILED:
							result = createSuccessResponse(reportDTO);
							break;

						default:
							result = createSuccessResponse(reportDTO.getId());
						}
					} catch (Exception e) {
						log.error("createReport failed.", e);
						result = createErrorResponse(ErrorCodes.CREATE_ENTITY_ERROR);
					}
				} else {
					result = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
				}
			} else {
				result = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
			}
		}
		return result;
	}

	private boolean isScopeAllowed(VisibilityEnum visibility, HttpServletRequest request) {
		boolean isScopeAllowed = false;
		if (visibility.equals(VisibilityEnum.PRIVATE)
				|| request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString())) {
			isScopeAllowed = true;
		} else {
			switch (visibility) {
			case SCOPE:
				isScopeAllowed = request.isUserInRole(ReportFeatureEnum.SHARE_REPORT_SCOPE.toString());
				break;
			case PUBLIC:
				isScopeAllowed = request.isUserInRole(ReportFeatureEnum.SHARE_REPORT_PUBLIC.toString());
				break;
			}
		}
		return isScopeAllowed;
	}

	@PUT
	@Path("/share/{id}/{visibility}")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response shareReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@PathParam("visibility") String visibility, @HeaderParam("scopeName") String scopeName,
			@HeaderParam("roleName") String roleName) {

		String username = request.getRemoteUser();
		VisibilityEnum newVisibility = VisibilityEnum.getByName(visibility);

		boolean isAdmin;

		log.debug("{} is requesting shareReport(...), with a ID={} with isShared={}", username, id, visibility);

		ReportFeatureEnum requiredFeature = null;

		switch (newVisibility) {
		case SCOPE:
			requiredFeature = ReportFeatureEnum.SHARE_REPORT_SCOPE;
			break;
		case PUBLIC:
			requiredFeature = ReportFeatureEnum.SHARE_REPORT_PUBLIC;
			break;
		default: // it is private scope which does not require any feature
			break;
		}

		Response restResponse;

		if (requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
			restResponse = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
		} else {

			try {
				Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName,
						scopeName);
				isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());

				// it's just a visibility update, therefore the permitted service layers don't
				// matter much and we pass null
				ReportDTO reportToUpdate = reportService.findById(features, id, username, scopeName, isAdmin, null);

				if (reportToUpdate != null) {
					reportToUpdate.setVisibility(newVisibility);

					reportService.share(id, reportToUpdate.getCreatedBy(), reportToUpdate.getScopeName(), isAdmin,
							newVisibility);

					restResponse = createSuccessResponse(AuthorizationCheckUtil
							.listAllowedVisibilityOptions(reportToUpdate.getCreatedBy(), username, features));
				} else {
					restResponse = createErrorResponse(ErrorCodes.ENTRY_NOT_FOUND);
				}
			} catch (Exception e) {

				log.error("Sharing report failed.", e);

				return createErrorResponse(e.getMessage());

			}
		}

		return restResponse;
	}

	@POST
	@Path("/execute/{id}")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response runReportV2(@Context HttpServletRequest request, @PathParam("id") Long id,
							  @HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName,
							  DisplayFormat format) {

		String username = request.getRemoteUser();

		log.debug("{} is requesting runReport(...), with a ID={}", username, id);

		try {
			Map additionalProperties = (Map) format.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);
			DateTime dateTime = DateUtils.UI_FORMATTER.parseDateTime((String) additionalProperties.get(TIMESTAMP));
			List<AreaIdentifierType> areaRestrictions = getRestrictionAreas(username, scopeName, roleName);
			Boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());
			Boolean withActivity = request.isUserInRole(ActivityFeaturesEnum.ACTIVITY_ALLOWED.value());

			ReportResult reportExecutionByReportId = reportExecutionService.getReportExecutionByReportIdV2(id, username, scopeName, areaRestrictions, dateTime, isAdmin, withActivity, format, null, null);

			try {
				ExecutionResultDTO executionResult = reportResultMapper.map(reportExecutionByReportId, format);
				ObjectNode rootNode = mapToGeoJson(executionResult);
				return createSuccessResponse(rootNode);

			} catch (Exception e) {
				log.error("Error mapping report result to dto");
				return createErrorResponse(e.getMessage());
			}

		} catch (Exception e) {
			log.error("Report execution failed.", e);
			return createErrorResponse(e.getMessage());
		}
	}

	@POST
	@Path("/old/execute/{id}")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response runReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName,
			DisplayFormat format) {

		String username = request.getRemoteUser();

		log.debug("{} is requesting runReport(...), with a ID={}", username, id);

		try {
			Map additionalProperties = (Map) format.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);
			DateTime dateTime = DateUtils.UI_FORMATTER.parseDateTime((String) additionalProperties.get(TIMESTAMP));
			List<AreaIdentifierType> areaRestrictions = getRestrictionAreas(username, scopeName, roleName);
			Boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());
			Boolean withActivity = request.isUserInRole(ActivityFeaturesEnum.ACTIVITY_ALLOWED.value());

			ExecutionResultDTO reportExecutionByReportId = reportExecutionService.getReportExecutionByReportId(id,
					username, scopeName, areaRestrictions, dateTime, isAdmin, withActivity, format);

			ObjectNode rootNode = mapToGeoJson(reportExecutionByReportId);
			return createSuccessResponse(rootNode);

		} catch (Exception e) {
			log.error("Report execution failed.", e);
			return createErrorResponse(e.getMessage());
		}
	}

	private ObjectNode mapToGeoJson(ExecutionResultDTO dto) throws IOException {

		ObjectNode rootNode;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
		rootNode = mapper.createObjectNode();
		StringWriter stringWriter = new StringWriter();

		GeometryMapper.INSTANCE.featureCollectionToGeoJson(dto.getMovements(), stringWriter);

		rootNode.set("movements", mapper.readTree(stringWriter.toString()));

		stringWriter.getBuffer().setLength(0);
		GeometryMapper.INSTANCE.featureCollectionToGeoJson(dto.getSegments(), stringWriter);
		rootNode.set("segments", mapper.readTree(stringWriter.toString()));

		rootNode.putPOJO("tracks", dto.getTracks());
		rootNode.putPOJO("trips", dto.getTrips());

		ObjectNode activityNode = new FeatureToGeoJsonJacksonMapper().convert(dto.getActivities());
		rootNode.putPOJO("activities", activityNode);

		rootNode.putPOJO("criteria", dto.getFaCatchSummaryDTO());

		return rootNode;
	}

	@POST
	@Path("/execute/")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response runReport(Report report) {

		String username = servletRequest.getRemoteUser();
		log.trace("{} is requesting runReport(...), with a report={}", username, report);

		int order = 1;

		report.setReportType(report.getReportType().toUpperCase()); // FIXME

        List<CriteriaFilterDTO> criteriaFilter = report.getFilterExpression().getCriteriaFilter();

        if (CollectionUtils.isNotEmpty(criteriaFilter)){
            for (CriteriaFilterDTO criteriaFilterDTO : criteriaFilter){
                criteriaFilterDTO.setOrderSequence(order++);
            }
        }

        try {
			Map additionalProperties = (Map) report.getAdditionalProperties().get(ADDITIONAL_PROPERTIES);
			String speedUnitString = additionalProperties.get(SPEED_UNIT).toString();
			String distanceUnitString = additionalProperties.get(DISTANCE_UNIT).toString();
			VelocityType velocityType = VelocityType.valueOf(speedUnitString.toUpperCase());
			LengthType lengthType = LengthType.valueOf(distanceUnitString.toUpperCase());

			DisplayFormat displayFormat = new DisplayFormat(velocityType, lengthType);
			List<AreaIdentifierType> areaRestrictions = getRestrictionAreas(username, scopeName, roleName);
			Boolean withActivity = servletRequest.isUserInRole(ActivityFeaturesEnum.ACTIVITY_ALLOWED.value());

			ExecutionResultDTO resultDTO = reportExecutionService.getReportExecutionWithoutSave(report,
					areaRestrictions, username, withActivity, displayFormat);
			ObjectNode rootNode = mapToGeoJson(resultDTO);
			return createSuccessResponse(rootNode);

		} catch (Exception e) {
			log.error("Report execution failed.", e);
			return createErrorResponse(e.getMessage());
		}
	}

	private List<AreaIdentifierType> getRestrictionAreas(String username, String scopeName, String roleName)
			throws ServiceException {
		List<Dataset> datasets = usmService.getDatasetsPerCategory(USMSpatial.USM_DATASET_CATEGORY, username,
				USMSpatial.APPLICATION_NAME, roleName, scopeName);
		List<AreaIdentifierType> areaRestrictions = new ArrayList<>(datasets.size());

		for (Dataset dataset : datasets) {
			int lastIndexDelimiter = dataset.getDiscriminator().lastIndexOf(USMSpatial.DELIMITER);

			if (lastIndexDelimiter > -1) {
				AreaIdentifierType areaIdentifierType = new AreaIdentifierType();
				// add AREATYPE/AREA_ID into a map
				AreaType areaType = AreaType.valueOf(dataset.getDiscriminator().substring(0, lastIndexDelimiter));
				String areaId = dataset.getDiscriminator().substring(lastIndexDelimiter + 1);

				if (areaType != null && StringUtils.isNotBlank(areaId)) {
					areaIdentifierType.setAreaType(areaType);
					areaIdentifierType.setId(areaId);
					areaRestrictions.add(areaIdentifierType);
				}
			}
		}

		return areaRestrictions;
	}

	@POST
	@Path("/default/{id}")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Response defaultReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName,
			Map<String, Object> payload) {

		final String username = request.getRemoteUser();
		final String appName = getApplicationName(request);
		Boolean override = false;

		if (payload != null) {
			override = Boolean.valueOf(String.valueOf(payload.get("override")));
		}

		Response response;
		try {

			String defaultId = usmService.getUserPreference(DEFAULT_REPORT_ID, username, appName, roleName, scopeName);
			Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
			if (!StringUtils.isEmpty(defaultId) && !override) {
				response = createErrorResponse("TRYING TO OVERRIDE ALREADY EXISTING VALUE");
			} else {
				boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());
				ReportDTO byId = reportService.findById(features, id, username, scopeName, isAdmin, null);
				if (byId == null){
					response = createErrorResponse("TRYING TO SET UN-EXISTING REPORT AS DEFAULT");
				} else {
					usmService.putUserPreference(DEFAULT_REPORT_ID, String.valueOf(id), appName, scopeName, roleName, username);
					response = createSuccessResponse();
				}
			}
		} catch (ServiceException e) {
			log.error("Default report saving failed.", e);
			response = createErrorResponse(e.getMessage());
		}

		return response;
	}
}