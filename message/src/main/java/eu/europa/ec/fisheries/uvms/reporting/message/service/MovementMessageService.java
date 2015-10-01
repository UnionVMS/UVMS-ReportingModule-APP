package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.message.MessageService;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;

import javax.jms.JMSException;
import java.util.List;

public interface MovementMessageService extends MessageService {

    public List<MovementMapResponseType> getMovementMap(MovementQuery movementQuery) throws JMSException, MessageException, ModelMapperException;

}
