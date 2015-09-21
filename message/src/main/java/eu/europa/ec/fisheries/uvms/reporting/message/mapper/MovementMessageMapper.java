package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

public interface MovementMessageMapper {

   String mapToGetMovementMapByQueryRequest(MovementQuery query) throws ModelMarshallException;

   List<MovementMapResponseType> mapToMovementMapResponse(TextMessage message) throws ModelMapperException, JMSException;

}
