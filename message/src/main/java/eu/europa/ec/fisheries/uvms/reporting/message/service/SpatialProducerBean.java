package eu.europa.ec.fisheries.uvms.reporting.message.service;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

@Stateless
@Local
public class SpatialProducerBean extends AbstractProducer {
	
	@Resource(mappedName = MessageConstants.QUEUE_MODULE_SPATIAL)
    private Destination destination;

	@Override
	protected Destination getDestination() {
		return destination;
	}
}
