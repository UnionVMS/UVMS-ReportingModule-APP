package eu.europa.ec.fisheries.uvms.reporting.rest.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockingUtils;

/**
 * Servlet Filter implementation class SessionInitFilter
 */
//@WebFilter(filterName = "3_SessionInitFilter")
public class SessionInitFilter implements Filter {
	  private static final Logger LOGGER = LoggerFactory.getLogger(SessionInitFilter.class);

    /**
     * Default constructor. 
     */
    public SessionInitFilter() {
       
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		LOGGER.debug("foFilter() [START]");
		
		String currentUserScope = request.getParameter(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE);
		
		LOGGER.info("Requested user scope is:" + currentUserScope);
		
		if (StringUtils.isNotBlank(currentUserScope)) {
			HttpSession session = request.getSession(true);
			Object sessionAttr = session.getAttribute(RestConstants.SESSION_ATTR_ACTIVE_USER_SCOPE);

			//if we don't have any user context in the session, or if we have - but it's not the active one
			//we must set the current one as a session attribute
			if (!(sessionAttr instanceof Context)
					|| ! ((Context) sessionAttr).getScope().getScopeName().equals(currentUserScope)) {
				//TODO get from USM the roles/features linked to this particular context 
				//and set it as session attribute with the name "SESSION_ATTR_ACTIVE_USER_CONTEXT"
				//For the moment it will be mocked!!!!!!!
				
				Context currentContext = MockingUtils.createContext(currentUserScope);//this should be replaced by a real USM call
				session.setAttribute(RestConstants.SESSION_ATTR_ACTIVE_USER_SCOPE, currentContext);
				LOGGER.info("User scope is set as a HTTP session attribute. Scope name:" + currentUserScope);
			}
		// pass the request along the filter chain
			chain.doFilter(request, response);
		} else {
			LOGGER.error("Current scope name parameter is missing in the HTTP request.");
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "Current scope name parameter is missing in the HTTP request.");
		}
		
		LOGGER.debug("foFilter() [END]");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
		
	}

}
