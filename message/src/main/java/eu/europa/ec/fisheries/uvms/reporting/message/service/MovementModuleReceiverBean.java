package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;

@Stateless
@LocalBean
public class MovementModuleReceiverBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.QUEUE_REPORTING)
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}
