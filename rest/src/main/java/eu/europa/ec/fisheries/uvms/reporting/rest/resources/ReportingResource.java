package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.domain.Report;
import eu.europa.ec.fisheries.uvms.reporting.rest.security.IsUserAllowed;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportLocal;

@Stateless
@Path("/report")
public class ReportingResource {

    final static Logger LOG = LoggerFactory.getLogger(ReportingResource.class);
    
    @Context
	private UriInfo context;
	
    @EJB
    private ReportLocal reportService;
    
    @GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
    @IsUserAllowed(features = { RestConstants.FEATURE_LIST_REPORTS })
    public List<Report> listReports(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @QueryParam(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE) String scope) {
    	
    	LOG.warn("Selected scope is " + scope);

    	reportService.findAvailable(userID, scopeID)
    	return null;
    }
    
    @GET
	@Path("/list2")
	@Produces(MediaType.APPLICATION_JSON)
    public List<Report> listReports2(@Context HttpServletRequest request, 
    				@Context HttpServletResponse response, @QueryParam(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE) String scope) {
    	
    	LOG.warn("Selected scope is " + scope);
    	
//    	if (StringUtils.isNotBlank(username)) {
//    		
//    	} else {
//    		//return access forbidden
//    	}
    	
    	return null;
    }
    
    
}
