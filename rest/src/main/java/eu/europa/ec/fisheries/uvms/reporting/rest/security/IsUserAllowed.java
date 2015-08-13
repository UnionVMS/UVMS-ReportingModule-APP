package eu.europa.ec.fisheries.uvms.reporting.rest.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

@Inherited
@IsUserAuthenticated
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsUserAllowed {

	@Nonbinding String[] allFeatures() default {};
	@Nonbinding String[] oneOfAllFeatures() default {};
}
