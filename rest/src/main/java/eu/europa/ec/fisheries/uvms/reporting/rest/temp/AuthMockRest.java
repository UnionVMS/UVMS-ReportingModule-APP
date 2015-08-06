package eu.europa.ec.fisheries.uvms.reporting.rest.temp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;


@Path("/mock")
public class AuthMockRest {

	public static final String SESSION_ATTR_USER = "SESSION_ATTR_USER";
	
//	public static final String SESSION_ATTR_ROLES = "SESSION_ATTR_ROLES";
	
	
	@GET
	@Path("/login/{username}/{scope}/{roles}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@Context HttpServletRequest request, 
			@Context HttpServletResponse response, @PathParam("username") String username, @PathParam("scope") String scope, @PathParam("roles") String rolesString) {
		HttpSession session = request.getSession(true);
		
		session.setAttribute(SESSION_ATTR_USER, username);
		
	/*	String[] roles = rolesString.split(",");
		Set<String> rolesSet = new HashSet<String>();
		
		for (String role : roles) {
			rolesSet.add(role);
		}
		
		session.setAttribute(SESSION_ATTR_ROLES, rolesSet);*/
		
		eu.europa.ec.fisheries.uvms.reporting.rest.domain.Context fakeContext = MockingUtils.createContext(scope);
		
		session.setAttribute(RestConstants.SESSION_ATTR_ACTIVE_USER_SCOPE, fakeContext);
		
		return username + " successfully logged in with the following context: " + fakeContext.toString();
	}
	
	@GET
	@Path("logout")
	@Produces(MediaType.TEXT_PLAIN)
	public String logout(@Context HttpServletRequest request, 
			@Context HttpServletResponse response) {
		String username = (String) request.getSession().getAttribute(SESSION_ATTR_USER);
		
		request.getSession().removeAttribute(SESSION_ATTR_USER);
		request.getSession().removeAttribute(RestConstants.SESSION_ATTR_ACTIVE_USER_SCOPE);
//		request.getSession().removeAttribute(SESSION_ATTR_ROLES);
		
		return username + " successfully logged out";
	}
	
	
}
