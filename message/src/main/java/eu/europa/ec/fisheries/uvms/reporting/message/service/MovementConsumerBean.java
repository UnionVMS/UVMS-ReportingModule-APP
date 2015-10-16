package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * //TODO create test
 */
public class MovementConsumerBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.COMPONENT_MESSAGE_IN_QUEUE_NAME)
    private Destination destination;


    @Override
    protected Destination getDestination() {
        return destination;
    }
}
