package eu.europa.ec.fisheries.uvms.reporting.rest.security;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.Feature;
import eu.europa.ec.fisheries.uvms.reporting.model.Scope;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.AuthMockRest;


@IsUserAllowed 
@Interceptor
public class AuthorizationInterceptor implements Serializable {
	
	final static Logger LOG = LoggerFactory.getLogger(AuthorizationInterceptor.class);

	@AroundInvoke
	public Object checkAuthorization(final InvocationContext ic) throws Exception{
		System.out.println("AuthorizationInterceptor INVOKED");
		/*LOG.info("Reporting REST authorization check BEGIN.");
		
		IsUserAllowed isUserAllowed = ic.getMethod().getAnnotation(IsUserAllowed.class);
		
		//check if the method requires authorization checks. 
		//Not all methods may have @IsUserAllowed annotation
		if (isUserAllowed != null) {
			String[] featuresRequired = isUserAllowed.features();
			
			LOG.info("Checking for the following application features: " + featuresRequired.toString());
			
			Object[] methodParams = ic.getParameters();
			
			HttpServletRequest request = null;
			HttpServletResponse response = null;
			
			for (Object param : methodParams) {
				if (param instanceof HttpServletRequest)
					request = (HttpServletRequest) param;
				else if (param instanceof HttpServletResponse)
						response = (HttpServletResponse) param;
					  
			}
			
			Context userContext = (Context) request.getSession().getAttribute(RestConstants.SESSION_ATTR_ACTIVE_USER_SCOPE);
			Set<Feature> features = userContext.getRole().getFeatures();
			
			LOG.info("These are the allowed user application features: " + features.toString());
			
			for (String featureToCheck : featuresRequired) {
				boolean isAllowed = false;
				
				for (Feature currentUserFeature : features) {
					//TODO check for application name == reporting
					if (featureToCheck.equals(currentUserFeature.getFeatureName())) {
						isAllowed = true;
						break;
					}
				}
				
				if (!isAllowed) {
					String username = request.getUserPrincipal().getName();
					String errorMsg = username + " doesn't have feature [" + featureToCheck + "] access, which is required by method - " + ic.getMethod().getName();
					LOG.error(errorMsg);
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMsg);
					break;
				} else {
					return ic.proceed();
				}
			}
			
			LOG.info("Reporting REST authorization check END.");
			return null;
		} else {*/
			return ic.proceed();
		/*}*/
	}
}
