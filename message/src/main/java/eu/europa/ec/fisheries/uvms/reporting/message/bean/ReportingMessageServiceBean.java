/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.message.bean;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageEvent;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.JAXBMarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.jms.*;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.CONNECTION_FACTORY;

/**
 * Created by padhyad on 3/18/2016.
 */

@Stateless
@LocalBean
@Slf4j
public class ReportingMessageServiceBean extends AbstractMessageService {

    @Resource(mappedName = MessageConstants.QUEUE_REPORTING_EVENT)
    private Destination request;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Override
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    protected Destination getEventDestination() {
        return request;
    }

    @Override
    protected Destination getResponseDestination() {
        return null;
    }

    @Override
    public String getModuleName() {
        return "reporting";
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleErrorResponseMessage(@Observes @ReportingMessageErrorEvent ReportingMessageEvent message) {
        try {
            log.info("Sending message back to recipient from SpatialModule with correlationId {} on queue: {}", message.getMessage().getJMSMessageID(),
                    message.getMessage().getJMSReplyTo());
            Session session = connectToQueue();
            String data = JAXBMarshaller.marshall(message.getFault());
            TextMessage response = session.createTextMessage(data);
            response.setJMSCorrelationID(message.getMessage().getJMSMessageID());
            session.createProducer(message.getMessage().getJMSReplyTo()).send(response);
        } catch (JMSException | ReportingModelException e) {
            log.error("[ Error when returning module spatial request. ] {} {}", e.getMessage(), e.getStackTrace(), e);
        } finally {
            disconnectQueue();
        }
    }
}