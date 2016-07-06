/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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