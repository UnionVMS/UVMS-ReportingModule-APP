package eu.europa.ec.fisheries.uvms.reporting.rest.utils;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import lombok.extern.slf4j.Slf4j;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Created by padhyad on 7/4/2016.
 */
@Interceptor
@Slf4j
public class ReportingExceptionInterceptor extends UnionVMSResource {

    @AroundInvoke
    public Object createResponse(final InvocationContext ic) {
        log.info("ExceptionInterceptor received");
        try {
            return ic.proceed();
        } catch (IllegalArgumentException e) {
            return createErrorResponse(ErrorCodes.INPUT_NOT_SUPPORTED);
        } catch (Exception e) {
            if (e.getCause() instanceof ReportingServiceException) {
                return createErrorResponse(((ReportingServiceException)e.getCause()).getMessage());
            }
            if (e.getCause() instanceof RuntimeException) {
                return createErrorResponse(((RuntimeException)e.getCause()).getMessage());
            }
            return createErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR);
        }
    }
}
