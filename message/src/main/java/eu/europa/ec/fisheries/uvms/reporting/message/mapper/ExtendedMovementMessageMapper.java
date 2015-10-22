package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

public class ExtendedMovementMessageMapper extends MovementModuleRequestMapper {

    public static String mapToGetMovementMapByQueryRequest(final MovementQuery query) throws ModelMarshallException {
        if (query == null){
           throw new IllegalArgumentException("Movementquery can not be null.");
        }
        return getMapToGetMovementMapByQueryRequest(query);
    }

    private static String getMapToGetMovementMapByQueryRequest(final MovementQuery query) throws ModelMarshallException {
        return mapToGetMovementMapByQueryRequest(query);
    }

    public static List<MovementMapResponseType> mapToMovementMapResponse(final TextMessage message) throws ModelMapperException, JMSException {
        if (message == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        return getMapToMovementMapResponse(message);
    }

    private static List<MovementMapResponseType> getMapToMovementMapResponse(final TextMessage message) throws ModelMapperException, JMSException {
        return mapToMovementMapResponse(message);
    }
}
