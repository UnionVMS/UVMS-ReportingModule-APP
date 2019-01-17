package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class AuditResponseConsumerBean extends AbstractConsumer {
    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_AUDIT;
    }
}
