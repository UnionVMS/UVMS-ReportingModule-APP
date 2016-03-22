package eu.europa.ec.fisheries.uvms.reporting.message.bean;

import eu.europa.ec.fisheries.uvms.reporting.message.event.GetReportStartAndEndDateEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageEvent;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.ReportingModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRQ;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportingModuleMethod;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportingModuleRequest;
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

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;

/**
 * Created by padhyad on 3/18/2016.
 */
@MessageDriven(mappedName = QUEUE_MODULE_SPATIAL, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = QUEUE_MODULE_REPORTING_NAME)
})
@Slf4j
public class ReportingEventMDB implements MessageListener {

    private static int REPORTING_MESSAGE = 1700;

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
