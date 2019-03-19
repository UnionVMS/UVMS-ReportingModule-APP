/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.message.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.message.util.JAXBMarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;

@Stateless
@LocalBean
@Slf4j
public class ReportingMessageServiceBean extends AbstractProducer {

    public String getDestinationName() {
        return MessageConstants.QUEUE_REPORTING_EVENT;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleErrorResponseMessage(@Observes @ReportingMessageErrorEvent ReportingMessageEvent messageWrap) {
        try {
            String data = JAXBMarshaller.marshall(messageWrap.getFault());
            sendResponseMessageToSender(messageWrap.getMessage(), data);
        } catch (MessageException | ReportingModelException e) {
            log.error("[ Error when returning module spatial request. ] {} {}", e.getMessage(), e.getStackTrace(), e);
        }
    }
}
