package eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.MovementMessageMapper;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

public class MovementMessageMapperImpl implements MovementMessageMapper {

    @Override
    public String mapToGetMovementMapByQueryRequest(final MovementQuery query) throws ModelMarshallException {
        if (query == null){
           throw new IllegalArgumentException("Movementquery can not be null.");
        }
        return getMapToGetMovementMapByQueryRequest(query);
    }

    protected String getMapToGetMovementMapByQueryRequest(final MovementQuery query) throws ModelMarshallException {
        return MovementModuleRequestMapper.mapToGetMovementMapByQueryRequest(query);
    }

    @Override
    public List<MovementMapResponseType> mapToMovementMapResponse(final TextMessage message) throws ModelMapperException, JMSException {
        if (message == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        return getMapToMovementMapResponse(message);
    }

    protected List<MovementMapResponseType> getMapToMovementMapResponse(final TextMessage message) throws ModelMapperException, JMSException {
        return MovementModuleResponseMapper.mapToMovementMapResponse(message);
    }
}
