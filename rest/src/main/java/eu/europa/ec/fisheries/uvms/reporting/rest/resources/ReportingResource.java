package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.ReportFeature;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.json.JsonResponseInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.ReportDetailsDTOToReportMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.ReportToDTOMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportBean;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;

@Path("/report")
public class ReportingResource extends UnionVMSResource {

    final static Logger LOG = LoggerFactory.getLogger(ReportingResource.class);
    
    @Context
	private UriInfo context;
	
    @EJB
    private ReportBean reportService;
    
    private ReportToDTOMapper mapper = ReportToDTOMapper.INSTANCE;
    
    private ReportDetailsDTOToReportMapper reportDetailsMapper = ReportDetailsDTOToReportMapper.INSTANCE;
     
    @GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
    //@IsUserAllowed(allFeatures = { ReportFeature.LIST_REPORTS })
    public Response listReports(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @QueryParam(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE) long scopeId) {
    	String username = "georgi"; //request.getRemoteUser() should return the username
    	
    	LOG.info(username + " is requesting listReports(...), with a scopeId=" + scopeId);

    	Collection<eu.europa.ec.fisheries.uvms.reporting.model.Report> reportsList = reportService.findReports(username, scopeId);
    	
    	//eu.europa.ec.fisheries.uvms.reporting.model.Context userContext = (eu.europa.ec.fisheries.uvms.reporting.model.Context) request.getSession().getAttribute(RestConstants.SESSION_ATTR_ACTIVE_USER_SCOPE);
    	eu.europa.ec.fisheries.uvms.reporting.model.Context userContext = MockingUtils.createContext("someScope", 
    			ReportFeature.LIST_REPORTS, 
    			ReportFeature.DELETE_REPORT, 
    			ReportFeature.CREATE_REPORTS, 
    			ReportFeature.SHARE_REPORTS_GLOBAL,
    			ReportFeature.SHARE_REPORTS_SCOPE,
    			ReportFeature.MODIFY_PRIVATE_REPORT);
    	
    	return createSuccessResponse(mapper.reportsToReportDtos(reportsList, username, userContext));
    }
    
    @GET
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    //@IsUserAllowed(allFeatures = { ReportFeature.VIEW_REPORT})
    public Response getReport(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @PathParam("id") Long id) {
    	
    	String username = "georgi"; //request.getRemoteUser() should return the username
    	
    	LOG.info(username + " is requesting getReport(...), with an ID=" + id );
    	
    	Report report = reportService.findById(id); 
    	
    	Response restResponse;
    	
    	if (report != null) {
	    	restResponse = createSuccessResponse(reportDetailsMapper.reportToReportDetailsDto(report));
    	} else {
    		restResponse = createErrorResponse(ErrorCodes.ENTRY_NOT_FOUND);
    	}
    	
    	return restResponse;
    }
    
    @DELETE
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    //@IsUserAllowed(allFeatures = { ReportFeature.VIEW_REPORT})
    @Interceptors(value = {JsonResponseInterceptor.class})
    public Response deleteReport(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @PathParam("id") Long id) throws ReportingServiceException{
    	
    	String username = "georgi"; //request.getRemoteUser() should return the username
    	
    	LOG.info(username + " is requesting deleteReport(...), with a ID=" + id );
    	
    	try {
    		reportService.delete(id);
    	} catch (ReportingServiceException exc) {
    		throw new ProcessingException(ErrorCodes.DELETE_FAILED);
    	}
    	
    	return createSuccessResponse();
    }

    
    @PUT
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    //@IsUserAllowed(oneOfAllFeatures = { ReportFeature.MODIFY_PRIVATE_REPORT, ReportFeature.MODIFY_SHARED_REPORTS}) 
    public Response modifyReport(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, ReportDetailsDTO report,@PathParam("id") Long id) {
    	
    	String username = "georgi"; //request.getRemoteUser() should return the username
    	
    	LOG.info(username + " is requesting modifyReport(...), with a ID=" + report.getId());
    	
    	reportService.update(reportDetailsMapper.reportDetailsDtoToReport(report));//TODO handle better merge because we pass not all the fields directly from the front-end
    	
    	return createSuccessResponse();
    }
    
    @POST
   	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@IsUserAllowed(oneOfAllFeatures = { ReportFeature.MODIFY_PRIVATE_REPORT, ReportFeature.MODIFY_SHARED_REPORTS}) 
    public Response createReport(@Context HttpServletRequest request, 
   				@Context HttpServletResponse response, ReportDetailsDTO report) {
   	
	   	String username = "georgi"; //request.getRemoteUser() should return the username
	   	
	   	LOG.info(username + " is requesting createReport(...), with a ID=" + report.getId());

	   	Report newReport = reportDetailsMapper.reportDetailsDtoToReport(report);
	   	newReport.setCreatedOn(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
	   	newReport.setCreatedBy(username);
	   	newReport.setFilterExpression("To be implemented");

	   	reportService.create(newReport);
	   	return createSuccessResponse();
    }
    
    @PUT
    @Path("/share/{id}/{visibility}")
   	@Produces(MediaType.APPLICATION_JSON)
    //@IsUserAllowed(oneOfAllFeatures = { ReportFeature.MODIFY_PRIVATE_REPORT, ReportFeature.MODIFY_SHARED_REPORTS}) 
    public Response shareReport(@Context HttpServletRequest request, 
   				@Context HttpServletResponse response, @PathParam("id") Long id, @PathParam("visibility") String visibility) {
   	
    	String username = "georgi"; //request.getRemoteUser() should return the username
   	
    	LOG.info(username + " is requesting shareReport(...), with a ID=" + id + " with isShared=" + visibility);
   	
    	Report reportToUpdate = reportService.findById(id);
    	reportToUpdate.setVisibility(VisibilityEnum.valueOf(visibility));
    	reportService.update(reportToUpdate);
    	
    	return  createSuccessResponse();
    }
    
    
    @GET
    @Path("/execute/{id}")
   	@Produces(MediaType.APPLICATION_JSON)
    //@IsUserAllowed(oneOfAllFeatures = { ReportFeature.MODIFY_PRIVATE_REPORT, ReportFeature.MODIFY_SHARED_REPORTS}) 
    public Response runReport(@Context HttpServletRequest request, 
   				@Context HttpServletResponse response, @PathParam("id") Long id) {
   	
    	String username = "georgi"; //request.getRemoteUser() should return the username
   	
    	LOG.info(username + " is requesting shareReport(...), with a ID=" + id);
		//TODO implement the filter parser and the actual search execution
    	reportService.executeReport(username, id);
    	return createSuccessResponse();
    }
    
}
