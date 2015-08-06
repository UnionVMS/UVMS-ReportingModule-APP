package eu.europa.ec.fisheries.uvms.reporting.rest.temp;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@MockAuthentication
public class AuthMockInterceptor implements Serializable {
	final static Logger LOG = LoggerFactory.getLogger(AuthMockInterceptor.class);
	
	//TODO MODIFY the HTTP Request to Provide fake User principal
	@AroundInvoke
	public Object mockAuthentication(InvocationContext ic) throws Exception {
		LOG.debug("mockAuthentication START");
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		Object[] params = ic.getParameters();
		for (Object param : params) {
			if (param instanceof HttpServletRequest) {
				request = (HttpServletRequest) param;
			} else if (param instanceof HttpServletResponse) {
				response = (HttpServletResponse) param;
			}
		}
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			HttpSession session = httpRequest.getSession();

			if (((HttpServletRequest) request).getSession(true).getAttribute(AuthMockRest.SESSION_ATTR_USER) != null) {
				String username = (String)session.getAttribute(AuthMockRest.SESSION_ATTR_USER);
				AuthRequestWrapper wrapper = new AuthRequestWrapper(httpRequest, username, null);
//				fc.doFilter(wrapper, response);
			} 
		}
		
		LOG.debug("mockAuthentication START");
		return ic.proceed();
	}
}
