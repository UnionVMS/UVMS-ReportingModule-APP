package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.ReportToDTOMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.security.IsUserAllowed;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportBean;

//@Stateless
@Path("/report")
public class ReportingResource {

    final static Logger LOG = LoggerFactory.getLogger(ReportingResource.class);
    
    @Context
	private UriInfo context;
	
    @EJB
    private ReportBean reportService;
    
    @Inject
    private ReportToDTOMapper mapper;
     
    @GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
    @IsUserAllowed(allFeatures = { RestConstants.FEATURE_LIST_REPORTS })
    public Response listReports(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @QueryParam(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE) long scopeId) {
    	String username = request.getRemoteUser();
    	
    	LOG.info(username + " is requesting listReports(...), with a scopeId=" + scopeId);

    	Collection<eu.europa.ec.fisheries.uvms.reporting.model.Report> reportsList = reportService.findReports(username, scopeId);
    	
    	//TODO enrich with the permissions flags
    	
    	return createResponse(HttpServletResponse.SC_OK, mapper.reportsToReportDtos(reportsList));
    }
    
    @GET
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @IsUserAllowed(allFeatures = { RestConstants.FEATURE_VIEW_REPORT})
    public Response getReport(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @PathParam("id") Long id) {
    	
    	String username = request.getRemoteUser();
    	
    	LOG.info(username + " is requesting getReport(...), with an ID=" + id );
    	
    	Report report = reportService.findById(id); 
    	return createResponse(HttpServletResponse.SC_OK, mapper.reportToReportDto(report));
    }
    
    @DELETE
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @IsUserAllowed(allFeatures = { RestConstants.FEATURE_VIEW_REPORT})
    public Response deleteReport(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @PathParam("id") Long id) {
    	
    	String username = request.getRemoteUser();
    	
    	LOG.info(username + " is requesting deleteReport(...), with a ID=" + id );
    	
    	reportService.delete(id);
    	return createResponse(HttpServletResponse.SC_OK, "success");
    }

    
    @PUT
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @IsUserAllowed(oneOfAllFeatures = { RestConstants.FEATURE_MODIFY_REPORT, RestConstants.FEATURE_MODIFY_SHARED_REPORTS}) 
    public Response modifyReport(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, ReportDTO report) {
    	
    	String username = request.getRemoteUser();
    	
    	LOG.info(username + " is requesting modifyReport(...), with a ID=" + report.getId());
    	
    	reportService.update(mapper.reportDtoToReport(report));
    	
    	return createResponse(HttpServletResponse.SC_OK, "success");
    }
    
    @POST
   	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @IsUserAllowed(oneOfAllFeatures = { RestConstants.FEATURE_MODIFY_REPORT, RestConstants.FEATURE_MODIFY_SHARED_REPORTS}) 
    public Response createReport(@Context HttpServletRequest request, 
   				@Context HttpServletResponse response, ReportDTO report) {
   	
	   	String username = request.getRemoteUser();
	   	
	   	LOG.info(username + " is requesting createReport(...), with a ID=" + report.getId());
	
	   	reportService.create(mapper.reportDtoToReport(report));
	   	return createResponse(HttpServletResponse.SC_OK, "success");
    }
    
    @PUT
    @Path("/share/{id}/{boolean}")
   	@Produces(MediaType.APPLICATION_JSON)
    @IsUserAllowed(oneOfAllFeatures = { RestConstants.FEATURE_MODIFY_REPORT, RestConstants.FEATURE_MODIFY_SHARED_REPORTS}) 
    public Response shareReport(@Context HttpServletRequest request, 
   				@Context HttpServletResponse response, @PathParam("id") Long id, @PathParam("boolean") Boolean isShared) {
   	
    	String username = request.getRemoteUser();
   	
    	LOG.info(username + " is requesting shareReport(...), with a ID=" + id + " with isShared=" + isShared);
   	
    	Report reportToUpdate = reportService.findById(id);
    	reportToUpdate.setIsShared(isShared);
    	reportService.update(reportToUpdate);
    	
    	return  createResponse(HttpServletResponse.SC_OK, "success");
    }
    
    
    @GET
    @Path("/execute/{id}")
   	@Produces(MediaType.APPLICATION_JSON)
    @IsUserAllowed(oneOfAllFeatures = { RestConstants.FEATURE_MODIFY_REPORT, RestConstants.FEATURE_MODIFY_SHARED_REPORTS}) 
    public Response runReport(@Context HttpServletRequest request, 
   				@Context HttpServletResponse response, @PathParam("id") Long id) {
   	
    	String username = request.getRemoteUser();
   	
    	LOG.info(username + " is requesting shareReport(...), with a ID=" + id);
    	//TODO 
    	return null;
    }
    
    /**
     * use Resteasy to put the provided HTTP status code and to convert the object into proper JSON response
     */
    private Response createResponse(int code, Object data) {
    	return Response.status(code).entity(data).build();
    	
    }
    
}
