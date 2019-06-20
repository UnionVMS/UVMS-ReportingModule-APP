/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityFeaturesEnum;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.rest.constants.ErrorCodes;
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
import eu.europa.ec.fisheries.uvms.reporting.service.enums.Projection;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.VelocityType;
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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.reporting.service.Constants.*;

@Slf4j
@Path("/report")
@NoArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportingResource {

	private static final String DEFAULT_REPORT_ID = "DEFAULT_REPORT_ID";
	private static final String USM_APPLICATION = "usmApplication";

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

	@EJB
	private USMService usmService;

	private String applicationName;

	@GET
	@Path("/list")
	@Interceptors(ReportingExceptionInterceptor.class)
	public Response listReports(@Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName,
			@HeaderParam("roleName") String roleName, @DefaultValue("Y") @QueryParam("existent") String existent)
			throws ServiceException, ReportingServiceException {
		Collection<ReportDTO> reportDTOs = listReportByUsernameAndScope(request, scopeName, roleName, existent, null);
		return Response.ok(reportDTOs).build();
	}

	@GET
	@Path("/list/lastexecuted/{numberOfReport}")
	@Interceptors(ReportingExceptionInterceptor.class)
	public Response listLastExecutedReports(@PathParam("numberOfReport") Integer numberOfReport,
			@DefaultValue("Y") @QueryParam("existent") String existent)
			throws ServiceException, ReportingServiceException {
		if (numberOfReport == null || numberOfReport == 0) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Number of last executed report cannot be null or 0").build();
		}
		Collection<ReportDTO> reportDTOs = listReportByUsernameAndScope(servletRequest, scopeName, roleName, existent,
				numberOfReport);
		return Response.ok(reportDTOs).build();
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

	// lazy loading of the app name from the web.xml
	private String getApplicationName(HttpServletRequest request) {
		if (applicationName == null) {
			applicationName = request.getServletContext().getInitParameter(USM_APPLICATION);
		}
		return applicationName;
	}

	@GET
	@Path("/{id}")
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
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getRootCause(e)).build();
		}

		if (report != null) {
			return Response.ok(report).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName) {

		String username = request.getRemoteUser();

		log.debug("{} is requesting deleteReport(...), with a ID={} and scopeName={}", username, id, scopeName);
		ReportDTO originalReport = null;
		boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());

		Response response = Response.ok().build();
		try {
			Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
			originalReport = reportService.findById(features, id, username, scopeName, isAdmin, null);
		} catch (Exception e) {
			String errorMsg = "Failed to get report.";
			log.error(errorMsg, e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getRootCause(e)).build();
		}

		if (originalReport == null) {
            response = Response.status(Response.Status.NOT_FOUND).build();
		}

		ReportFeatureEnum requiredFeature = AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(originalReport,
				username);

		if (requiredFeature != null && !request.isUserInRole(requiredFeature.toString())) {
			response = Response.status(Response.Status.UNAUTHORIZED).entity(ErrorCodes.NOT_AUTHORIZED).build();
		}

		try {
			reportService.delete(id, username, scopeName, isAdmin);
		} catch (Exception exc) {
			log.error("Report deletion failed.", exc);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorCodes.DELETE_FAILED).build();
		}

		return response;
	}

	@PUT
	@Path("/{id}")
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
				result = Response.status(Response.Status.UNAUTHORIZED).entity(ErrorCodes.NOT_AUTHORIZED).build();
			} else {
				ReportDTO update = reportService.update(report, username, originalReport.getWithMap(),
						originalReport.getMapConfiguration());
				switch (Projection.valueOf(projection.toUpperCase())) {

				case DETAILED:
					result = Response.ok(update).build();
					break;

				default:
					result = Response.ok(update.getId()).build();
				}
			}

		} catch (Exception exc) {
			log.error("Update failed.", exc);
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorCodes.UPDATE_FAILED).build();
		}
		return result;
	}

	@POST
	public Response createReport(@Context HttpServletRequest request, ReportDTO report,
			@DefaultValue("default") @QueryParam(value = "projection") String projection,
			@HeaderParam("scopeName") String scopeName) {
		Response result;
		String username = request.getRemoteUser();

		log.debug("{} is requesting createReport(...), with a ID={}, scopeName: {}, visibility: {}", username,
				report.getId(), scopeName, report.getVisibility());

		if (StringUtils.isBlank(scopeName)) {
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorCodes.USER_SCOPE_MISSING).build();
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
							result = Response.ok(reportDTO).build();
							break;

						default:
							result = Response.ok(reportDTO.getId()).build();
						}
					} catch (Exception e) {
						log.error("createReport failed.", e);
						result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorCodes.CREATE_ENTITY_ERROR).build();
					}
				} else {
					result = Response.status(Response.Status.UNAUTHORIZED).entity(ErrorCodes.NOT_AUTHORIZED).build();
				}
			} else {
				result = Response.status(Response.Status.UNAUTHORIZED).entity(ErrorCodes.NOT_AUTHORIZED).build();
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
	public Response shareReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@PathParam("visibility") String visibility, @HeaderParam("scopeName") String scopeName,
			@HeaderParam("roleName") String roleName) {

		String username = request.getRemoteUser();
		VisibilityEnum newVisibility = VisibilityEnum.getByName(visibility);

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
			restResponse = Response.status(Response.Status.UNAUTHORIZED).entity(ErrorCodes.NOT_AUTHORIZED).build();
		} else {
			try {
				Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName,
						scopeName);
				boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());

				// it's just a visibility update, therefore the permitted service layers don't matter much and we pass null
				ReportDTO reportToUpdate = reportService.findById(features, id, username, scopeName, isAdmin, null);

				if (reportToUpdate != null) {
					reportToUpdate.setVisibility(newVisibility);

					reportService.share(id, reportToUpdate.getCreatedBy(), reportToUpdate.getScopeName(), isAdmin, newVisibility);
					List<VisibilityEnum> visibilityEnums =
							AuthorizationCheckUtil.listAllowedVisibilityOptions(reportToUpdate.getCreatedBy(), username, features);
					restResponse = Response.ok(visibilityEnums).build();
				} else {
					restResponse = Response.status(Response.Status.UNAUTHORIZED).entity(ErrorCodes.ENTRY_NOT_FOUND).build();
				}
			} catch (Exception e) {
				log.error("Sharing report failed.", e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getRootCause(e)).build();
			}
		}
		return restResponse;
	}

	@POST
	@Path("/execute/{id}")
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
			return Response.ok(rootNode).build();

		} catch (Exception e) {
			log.error("Report execution failed.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getRootCause(e)).build();
		}
	}

	private ObjectNode mapToGeoJson(ExecutionResultDTO dto) throws IOException {

		ObjectNode rootNode;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
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
			return Response.ok(rootNode).build();

		} catch (Exception e) {
			log.error("Report execution failed.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getRootCause(e)).build();
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
	public Response defaultReport(@Context HttpServletRequest request, @PathParam("id") Long id,
			@HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName,
			Map<String, Object> payload) {

		final String username = request.getRemoteUser();
		final String appName = getApplicationName(request);
		boolean override = false;

		if (payload != null) {
			override = Boolean.parseBoolean(String.valueOf(payload.get("override")));
		}

		Response response;

		try {
			String defaultId = usmService.getUserPreference(DEFAULT_REPORT_ID, username, appName, roleName, scopeName);
			Set<String> features = usmService.getUserFeatures(username, getApplicationName(request), roleName, scopeName);
			if (!StringUtils.isEmpty(defaultId) && !override) {
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("TRYING TO OVERRIDE ALREADY EXISTING VALUE").build();
			} else {
				boolean isAdmin = request.isUserInRole(ReportFeatureEnum.MANAGE_ALL_REPORTS.toString());
				ReportDTO byId = reportService.findById(features, id, username, scopeName, isAdmin, null);
				if (byId == null){
					response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("TRYING TO SET UN-EXISTING REPORT AS DEFAULT").build();
				} else {
					usmService.putUserPreference(DEFAULT_REPORT_ID, String.valueOf(id), appName, scopeName, roleName, username);
					response = Response.ok().build();
				}
			}
		} catch (ServiceException e) {
			log.error("Default report saving failed.", e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getRootCause(e)).build();
		}
		return response;
	}
}
