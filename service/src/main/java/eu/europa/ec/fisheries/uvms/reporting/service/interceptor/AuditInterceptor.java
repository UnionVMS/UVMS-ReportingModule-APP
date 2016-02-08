package eu.europa.ec.fisheries.uvms.reporting.service.interceptor;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AuditService;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;

@IAuditInterceptor
@Interceptor
public class AuditInterceptor implements Serializable {

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

            ReportDTO report = (ReportDTO)result;

			if (report != null) {

				Long id = report.getId();

				sendAuditReport(auditAction, id);

			}			
			
		}

        else if (auditAction.equals(AuditActionEnum.MODIFY)) {

            ReportDTO report = (ReportDTO)parameters[0];

			if (report != null) {

				Long id = report.getId();

				sendAuditReport(auditAction, id);

			}			
			
		}

        else if (auditAction.equals(AuditActionEnum.DELETE)) {

			Long id = (Long)parameters[0];

			if (id != null) {

				sendAuditReport(auditAction, id);

			}

		}

        else if (auditAction.equals(AuditActionEnum.EXECUTE)) {

            Long id = (Long)parameters[2];

            if (id != null) {

                sendAuditReport(auditAction, id);

            }

        }

        else if (auditAction.equals(AuditActionEnum.SHARE)) {

            Long reportId = (Long)parameters[0];

            if (reportId != null) {
                sendAuditReport(auditAction, reportId);
            }

        }

        else {

			LOG.warn("Audit action cannot be intercepted");

		}

		return result;
	}
	
	private void sendAuditReport(AuditActionEnum auditAction, Long id) {
		try {
			LOG.info("Audit type received is : " + auditAction.getAuditType() + " ID : " + id);
			auditService.sendAuditReport(auditAction, id.toString());
		} catch (ReportingServiceException e) {
			LOG.error("Exception occurred while executing interceptor", e);
		}				
	}
}
