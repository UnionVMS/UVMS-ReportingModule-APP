package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.AuditProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.constants.ReportingServiceConstants;

@Stateless
@Local(value = AuditService.class)
public class AuditServiceBean implements AuditService {
	
	private static Logger LOG = LoggerFactory.getLogger(AuditServiceBean.class.getName());
	
	@EJB
	private AuditProducerBean auditProducerBean;

	@Override
	public void sendAuditReport(final AuditActionEnum auditActionEnum, final String objectId) throws ReportingServiceException {
		
		LOG.info("Audit report request received for : " + auditActionEnum.getAuditType());
		try {
			String msgToSend = AuditLogMapper.mapToAuditLog(ReportingServiceConstants.REPORTING_MODULE, auditActionEnum.getAuditType(), objectId);
			LOG.info("Sending JMS message to Audit : " + msgToSend);			
			auditProducerBean.sendModuleMessage(msgToSend, auditProducerBean.getDestination());
			
		} catch (MessageException e) {
			LOG.error("Exception in Sending Message to Audit Queue", e);
			throw new ReportingServiceException(e);
		} catch (AuditModelMarshallException e) {
			LOG.error("Audit model marshal exception", e);
			throw new ReportingServiceException(e);
		}
	}
}
