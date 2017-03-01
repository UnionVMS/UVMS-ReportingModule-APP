/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.uvms.message.JMSUtils;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AuditService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportingServiceConstants;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.naming.InitialContext;

import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.AuditMessageServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

@Stateless
@Local(value = AuditService.class)
public class AuditServiceBean implements AuditService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuditServiceBean.class.getName());

		
    private Destination auditResponse;
	
	@EJB
	private transient AuditMessageServiceBean auditProducerBean;


	@PostConstruct
    public void init() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (Exception e) {
            LOG.error("Failed to get InitialContext",e);
            throw new RuntimeException(e);
        }
        auditResponse = JMSUtils.lookupQueue(ctx, MessageConstants.QUEUE_AUDIT);
    }	
	
	@Override
	public void sendAuditReport(final AuditActionEnum auditActionEnum, final String objectId, final String userName) throws ReportingServiceException {
		
		LOG.info("Audit report request received for : " + auditActionEnum.getAuditType());
		try {
			String msgToSend = AuditLogMapper.mapToAuditLog(ReportingServiceConstants.REPORTING_MODULE, auditActionEnum.getAuditType(), objectId, userName);
			LOG.info("Sending JMS message to Audit : " + msgToSend);			
			auditProducerBean.sendModuleMessage(msgToSend,auditResponse);
			
			
		} catch (MessageException e) {
			LOG.error("Exception in Sending Message to Audit Queue", e);
			throw new ReportingServiceException(e);
		} catch (AuditModelMarshallException e) {
			LOG.error("Audit model marshal exception", e);
			throw new ReportingServiceException(e);
		}
	}
}