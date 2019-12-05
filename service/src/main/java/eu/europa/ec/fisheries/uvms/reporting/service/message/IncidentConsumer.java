package eu.europa.ec.fisheries.uvms.reporting.service.message;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncidentServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/" + MessageConstants.QUEUE_INCIDENT),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class IncidentConsumer implements MessageListener {

    @Inject
    private IncidentServiceBean incidentServiceBean;

    private static final Logger LOG = LoggerFactory.getLogger(IncidentConsumer.class);

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage tm = (TextMessage) message;
            TicketType ticket = tm.getBody(TicketType.class);

            String eventType = message.getStringProperty("eventName");
            switch (eventType) {
                case "Incident":
                    incidentServiceBean.createIncident(ticket);
                    break;
                case "IncidentUpdate":
                    incidentServiceBean.updateIncident(ticket);
                    break;
            }
        } catch (JMSException e) {
            LOG.error("Error while reading from Incident Queue and converting message to POJO.");
        }
    }
}
