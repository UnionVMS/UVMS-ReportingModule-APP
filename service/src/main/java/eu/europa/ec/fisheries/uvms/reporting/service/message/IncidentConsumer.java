package eu.europa.ec.fisheries.uvms.reporting.service.message;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/" + MessageConstants.QUEUE_INCIDENT),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class IncidentConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {

        // do it !!!!


    }
}

