package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

/**
 * Audit Service interface to provide entry point methods for all the services related to Audit module
 *
 */
public interface AuditService {
	
	/**
	 * Send Audit report to Audit module for CRUD operations on Report
	 * 
	 * @param auditActionEnum {@link AuditActionEnum}
	 * @param objectId {@link String}
	 * @throws ReportingServiceException {@link ReportingServiceException}
	 */
	public void sendAuditReport(final AuditActionEnum auditActionEnum, final String objectId) throws ReportingServiceException;

}
