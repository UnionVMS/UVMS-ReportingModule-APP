package eu.europa.ec.fisheries.uvms.reporting.service.interceptor;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AuditService;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;

@IAuditInterceptor
@Interceptor
public class AuditInterceptor implements Serializable {

	private static final long serialVersionUID = 7335928782218713261L;
	
	private final static Logger LOG = LoggerFactory.getLogger(AuditInterceptor.class.getName());
	
	@EJB
	private AuditService auditService;
	
	@AroundInvoke
	public Object interceptAuditAction(final InvocationContext ic) throws Exception {
		
		LOG.info("In the interceptor before method execution");
		Object result = ic.proceed();
		Object[] parameters = ic.getParameters();
		LOG.info("In the interceptor after method execution");
		IAuditInterceptor auditActionInterface = ic.getMethod().getAnnotation(IAuditInterceptor.class);
		AuditActionEnum auditAction = auditActionInterface.auditActionType();
		
		if (auditAction.equals(AuditActionEnum.CREATE)) {
			Report report = (Report)result;
			if (report != null) {
				Long id = report.getId();
				sendAuditReport(auditAction, id);
			}			
			
		} else if (auditAction.equals(AuditActionEnum.MODIFY)) {
			Report report = (Report)parameters[0];
			if (report != null) {
				Long id = report.getId();
				sendAuditReport(auditAction, id);
			}			
			
		} else if (auditAction.equals(AuditActionEnum.DELETE)) {
			Long id = (Long)parameters[0];
			if (id != null) {
				sendAuditReport(auditAction, id);
			}			
			
		} else {
			LOG.warn("Audit action cannot be intercepted");
		}
		return result;
	}
	
	private void sendAuditReport(AuditActionEnum auditAction, Long id) {
		try {
			LOG.info("Audit type received is : " + auditAction.getAuditType() + " ID : " + id);
			auditService.sendAuditReport(auditAction, id.toString());
		} catch (ReportingServiceException e) {
			LOG.error("Exception Occured while executing interceptor", e);
		}				
	}
}
