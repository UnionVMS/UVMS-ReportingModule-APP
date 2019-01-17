package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;

public class AuditResponseConsumerBean extends AbstractConsumer {
    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_AUDIT;
    }
}
