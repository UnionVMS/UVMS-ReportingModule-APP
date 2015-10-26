package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

public class ExtendedMovementMessageMapper extends MovementModuleRequestMapper {

    public static String mapToGetMovementMapByQueryRequest(final MovementQuery query) throws ModelMarshallException {
        if (query == null){
           throw new IllegalArgumentException("Movementquery can not be null.");
        }
        return MovementModuleRequestMapper.mapToGetMovementMapByQueryRequest(query);
    }

    public static List<MovementMapResponseType> mapToMovementMapResponse(final TextMessage message) throws ModelMapperException, JMSException {
        if (message == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        return MovementModuleResponseMapper.mapToMovementMapResponse(message);
    }

    public static Map<String, MovementMapResponseType> getMovementMap(List<MovementMapResponseType> movementMapResponseTypes) {
        return Maps.uniqueIndex(movementMapResponseTypes, new Function<MovementMapResponseType, String>() {
            public String apply(MovementMapResponseType from) {
                return from.getKey();
            }
        });
    }
}
