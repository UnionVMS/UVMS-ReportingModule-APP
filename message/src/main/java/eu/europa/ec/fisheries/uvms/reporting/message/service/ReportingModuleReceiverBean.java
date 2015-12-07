package eu.europa.ec.fisheries.uvms.reporting.message.service;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

@Stateless
@Local
public class ReportingModuleReceiverBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.QUEUE_REPORTING)
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}
