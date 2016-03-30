package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;

/**
 * Created by padhyad on 3/23/2016.
 */
@Stateless
@Local
public class RulesProducerBean extends AbstractProducer {

    @Resource(mappedName = MessageConstants.QUEUE_MODULE_RULES)
    private Destination destination;

    @Override
    protected Destination getDestination() {
        return destination;
    }
}
