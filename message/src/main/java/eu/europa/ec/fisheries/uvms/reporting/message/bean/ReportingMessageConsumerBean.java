/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.message.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.reporting.message.event.GetReportStartAndEndDateEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ReportingModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.model.ReportGetStartAndEndDateRQ;
import eu.europa.ec.fisheries.uvms.reporting.message.model.ReportingModuleMethod;
import eu.europa.ec.fisheries.uvms.reporting.message.model.ReportingModuleRequest;
import eu.europa.ec.fisheries.uvms.reporting.message.util.JAXBMarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = MessageConstants.QUEUE_MODULE_SPATIAL, activationConfig = {
        @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR, propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = MessageConstants.QUEUE_MODULE_REPORTING_NAME)
})
@Slf4j
public class ReportingMessageConsumerBean implements MessageListener {

    private static final int REPORTING_MESSAGE = 1700;

    @Inject
    @GetReportStartAndEndDateEvent
    private Event<ReportingMessageEvent> reportStartAndEndDateEvent;

    @Inject
    @ReportingMessageErrorEvent
    private Event<ReportingMessageEvent> reportingErrorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            ReportingModuleRequest request = JAXBMarshaller.unmarshall(textMessage, ReportingModuleRequest.class);
            ReportingModuleMethod method = request.getMethod();
            switch (method) {
                case GET_REPORT_START_END_DATE:
                    ReportGetStartAndEndDateRQ reportGetStartAndEndDateRQ = JAXBMarshaller.unmarshall(textMessage, ReportGetStartAndEndDateRQ.class);
                    ReportingMessageEvent reportDatesEvent = new ReportingMessageEvent(textMessage, reportGetStartAndEndDateRQ);
                    reportStartAndEndDateEvent.fire(reportDatesEvent);
                    break;
                default:
                    log.error("[ Not implemented method consumed: {} ]", method);
                    reportingErrorEvent.fire(new ReportingMessageEvent(textMessage, ReportingModuleResponseMapper.createFaultMessage(REPORTING_MESSAGE, "Method not implemented")));
            }
        } catch (ReportingModelException e) {
            log.error("[ Error when receiving message in Reporting Module. ]", e);
            reportingErrorEvent.fire(new ReportingMessageEvent(textMessage, ReportingModuleResponseMapper.createFaultMessage(REPORTING_MESSAGE, "Method not implemented")));
        }
    }
}
