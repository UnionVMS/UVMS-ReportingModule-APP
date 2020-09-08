package eu.europa.ec.fisheries.uvms.reporting.message.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;

@Stateless
@LocalBean
public class MovementResponseConsumerBean extends AbstractConsumer {
    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_MOVEMENT;
    }
}
