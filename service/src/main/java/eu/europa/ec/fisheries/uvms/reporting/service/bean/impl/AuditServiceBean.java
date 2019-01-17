/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.message.service.AuditMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.AuditResponseConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AuditService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportingServiceConstants;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.naming.InitialContext;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(value = AuditService.class)
@Slf4j
public class AuditServiceBean implements AuditService {
	
	@EJB
	private AuditMessageServiceBean auditProducerBean;

	@EJB
    private AuditResponseConsumerBean auditResponseConsumer;

	@Override
	public void sendAuditReport(final AuditActionEnum auditActionEnum, final String objectId, final String userName) throws ReportingServiceException {
        log.debug("Audit report request received for type = {} ", auditActionEnum.getAuditType());
        try {
			String msgToSend = AuditLogMapper.mapToAuditLog(ReportingServiceConstants.REPORTING_MODULE, auditActionEnum.getAuditType(), objectId, userName);
            log.trace("Sending JMS message to Audit {} ", msgToSend);
            auditProducerBean.sendModuleMessage(msgToSend, auditResponseConsumer.getDestination());
        } catch (MessageException e) {
            log.error("Exception in Sending Message to Audit Queue", e);
            throw new ReportingServiceException(e);
		} catch (AuditModelMarshallException e) {
            log.error("Audit model marshal exception", e);
            throw new ReportingServiceException(e);
		}
	}
}