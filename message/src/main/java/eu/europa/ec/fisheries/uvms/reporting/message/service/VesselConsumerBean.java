package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;

/**
 * //TODO create test
 */
@Stateless
@LocalBean
public class VesselConsumerBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.QUEUE_VESSEL)
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}
