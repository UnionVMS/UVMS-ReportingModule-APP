package eu.europa.ec.fisheries.uvms.reporting.rest.security;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.rest.temp.AuthMockInterceptor;

@IsUserAuthenticated
//@Interceptor
public class AuthenticationInterceptor implements Serializable {
	final static Logger LOG = LoggerFactory.getLogger(AuthenticationInterceptor.class);
	
	@AroundInvoke
	public Object checkAuthentication(final InvocationContext ic) throws Exception {
		LOG.debug("checkAuthentication START");
		
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
		
		if (((HttpServletRequest)request).getUserPrincipal() == null || ((HttpServletRequest)request).getUserPrincipal().getName() == null) {
			String errorMessage = "User authentication required!";
	    	LOG.error(errorMessage);
	    	((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
		} else {
			LOG.debug("checkAuthentication END");
			return ic.proceed();
		}
		
		return null;
	}
}
