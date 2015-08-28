package eu.europa.ec.fisheries.uvms.reporting.message.producer.bean;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

/**
 * Audit Producer Bean responsible to send messages to AUDIT module from Reporting
 * The implementation of sending message to the queue is handled in generic implementation of AbstractProducer
 * 
 * @see {@link AbstractProducer}
 *
 */
@Stateless
@LocalBean
public class AuditProducerBean extends AbstractProducer {

	@Resource(mappedName = MessageConstants.QUEUE_MODULE_AUDIT)
    private Destination auditModuleQueue;
	
	@Override
	public Destination getDestination() {
		return auditModuleQueue;
	}
}
