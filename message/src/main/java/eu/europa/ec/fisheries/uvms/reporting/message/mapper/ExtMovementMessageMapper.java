package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.validation.constraints.NotNull;
import java.util.*;

public class ExtMovementMessageMapper {

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
            public String apply(@NotNull MovementMapResponseType from) {
                return from.getKey();
            }
        });
    }

    public static List<MovementType> mapToMovementListResponse(TextMessage message) throws ModelMapperException, JMSException {
        if (message == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        return MovementModuleResponseMapper.mapToMovementListResponse(message);
    }

    public static String mapToGetMovementListByQueryRequest(MovementQuery query) throws ModelMarshallException {
        if (query == null){
            throw new IllegalArgumentException("Movementquery can not be null.");
        }
        return MovementModuleRequestMapper.mapToGetMovementListByQueryRequest(query);
    }

    public static Collection<? extends ListCriteria> movementListCriteria(Set<String> connectIds) {

        List<ListCriteria> criteria = new ArrayList<ListCriteria>();

        for(String id: connectIds){
            ListCriteria movementType = new ListCriteria();
            movementType.setKey(SearchKey.CONNECT_ID);
            movementType.setValue(id);
            criteria.add(movementType);
        }

        return criteria;
    }
}
