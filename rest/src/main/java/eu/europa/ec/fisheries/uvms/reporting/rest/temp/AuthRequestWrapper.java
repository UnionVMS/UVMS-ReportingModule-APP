package eu.europa.ec.fisheries.uvms.reporting.rest.temp;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class AuthRequestWrapper extends HttpServletRequestWrapper {

	private String username;
	private Set<String> roleSet;
	private Principal principal;

	public AuthRequestWrapper(HttpServletRequest request, String username, Set<String> roleSet) {
		super(request);
		this.username = username;
		this.roleSet = roleSet;
		this.principal = new UserPrincipalImpl(username);
	}

	public String getRemoteUser() {
	return username;
	}

	public Principal getUserPrincipal() {
	return principal;
	}

	public boolean isUserInRole(String roleName) {
	return roleSet.contains(roleName);
	}
}


class UserPrincipalImpl implements UserPrincipal {

	private String username;
	
	public UserPrincipalImpl(String username) {
		this.username = username;
	}
	
	@Override
	public String getName() {
		return this.username;
	}
	
}
