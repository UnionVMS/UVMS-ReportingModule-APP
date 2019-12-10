package eu.europa.ec.fisheries.uvms.reporting.service.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ec.fisheries.schema.movementrules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncidentServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = MessageConstants.QUEUE_INCIDENT, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "IncidentEvent"),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = MessageConstants.QUEUE_INCIDENT),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = MessageConstants.CONNECTION_FACTORY)
})
public class IncidentConsumer implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentConsumer.class);

    private ObjectMapper om = new ObjectMapper();

    @PostConstruct
    public void init() {
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Inject
    private IncidentServiceBean incidentServiceBean;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage tm = (TextMessage) message;
            String json = tm.getBody(String.class);

            TicketType ticket = om.readValue(json, TicketType.class);

            String eventType = message.getStringProperty("eventName");
            LOG.info("New message: " + eventType);
            System.out.println("New message: " + eventType);
            switch (eventType) {
                case "Incident":
                    incidentServiceBean.createIncident(ticket);
                    break;
                case "IncidentUpdate":
                    incidentServiceBean.updateIncident(ticket);
                    break;
            }
        } catch (Exception e) {
            LOG.error("Error while reading from Incident Queue", e);
            throw new EJBException(e);
        }
    }
}
