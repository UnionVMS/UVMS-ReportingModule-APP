package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * //TODO create test
 */
public class MovementProducerBean extends AbstractProducer {

    @Resource(mappedName = MessageConstants.COMPONENT_MESSAGE_IN_QUEUE)
    private Destination destination;

    @Override
    protected Destination getDestination() {
        return destination;
    }
}
