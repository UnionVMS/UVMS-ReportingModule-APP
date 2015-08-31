package eu.europa.ec.fisheries.uvms.reporting.rest.json;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;


@Interceptor
@Slf4j
public class JsonResponseInterceptor extends UnionVMSResource{
	
	@AroundInvoke
	public Object  createResponse(final InvocationContext ic) throws Exception {
		log.debug("JsonResponseInterceptor INVOKED");
		
		Response response = null;
		
		try {
				return ic.proceed();
		} catch(ProcessingException pexc) {
			response = createErrorResponse(pexc.getMessage());
		} catch(Exception e) { 
			response = createErrorResponse();
		}
			
		return response;
	}

	
}
