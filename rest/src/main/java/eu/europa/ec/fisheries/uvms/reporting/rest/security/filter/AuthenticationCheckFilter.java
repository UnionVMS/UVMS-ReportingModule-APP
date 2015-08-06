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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
//@WebFilter(filterName = "2_AuthenticationCheckFilter")
public class AuthenticationCheckFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationCheckFilter.class);

    /**
     * Default constructor. 
     */
    public AuthenticationCheckFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LOGGER.debug("foFilter() [START]");
		
		if (((HttpServletRequest)request).getUserPrincipal().getName() == null) {
			String errorMessage = "User authentication required!";
	    	LOGGER.error(errorMessage);
	    	((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
		} else {
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}
		
		LOGGER.debug("foFilter() [END]");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
