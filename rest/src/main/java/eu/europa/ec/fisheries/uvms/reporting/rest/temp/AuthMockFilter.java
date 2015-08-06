package eu.europa.ec.fisheries.uvms.reporting.rest.temp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//@WebFilter(filterName = "1_AuthMockFilter")
public class AuthMockFilter implements Filter {
	
	public void doFilter(ServletRequest request, 
			ServletResponse response,
			FilterChain fc) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			HttpSession session = httpRequest.getSession();

			if (((HttpServletRequest) request).getSession(true).getAttribute(AuthMockRest.SESSION_ATTR_USER) != null) {
				String username = (String)session.getAttribute(AuthMockRest.SESSION_ATTR_USER);
				AuthRequestWrapper wrapper = new AuthRequestWrapper(httpRequest, username, null);
				fc.doFilter(wrapper, response);
			} else {
				fc.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
