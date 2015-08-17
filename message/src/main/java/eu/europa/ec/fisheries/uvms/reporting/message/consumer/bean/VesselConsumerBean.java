package eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.jms.Queue;

/**
 * //TODO create test
 */
@Stateless
@LocalBean
public class VesselConsumerBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.QUEUE_VESSEL)
    private Queue vesselModuleQ;

    @Override
    public Destination getDestination() {
        return vesselModuleQ;
    }
}
