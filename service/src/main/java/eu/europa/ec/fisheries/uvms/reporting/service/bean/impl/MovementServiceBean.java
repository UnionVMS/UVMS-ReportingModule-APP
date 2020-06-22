/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.util.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.user.types.UserFault;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Slf4j
public class MovementServiceBean {

    @EJB
    private MovementModuleSenderBean movementSender;

    @EJB
    private ReportingModuleReceiverBean receiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Interceptors(SimpleTracingInterceptor.class)
    public Map<String, MovementMapResponseType> getMovementMap(FilterProcessor processor) throws ReportingServiceException {
        return ExtMovementMessageMapper.getMovementMap(getMovementMapResponseTypes(processor));
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Interceptors(SimpleTracingInterceptor.class)
    public Map<String, MovementMapResponseType> getMovementMapReporting(FilterProcessor processor) throws ReportingServiceException {
    	return ExtMovementMessageMapper.getMovementMap(getMovementMapResponseTypesReporting(processor));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<MovementMapResponseType> getMovement(FilterProcessor processor) throws ReportingServiceException {
        log.trace("getMovement({})", processor.toString());
        return getMovementMapResponseTypes(processor);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<MovementMapResponseType> getMovementReporting(FilterProcessor processor) throws ReportingServiceException {
    	log.trace("getMovement({})", processor.toString());
    	return getMovementMapResponseTypesReporting(processor);
    }

    private List<MovementMapResponseType> getMovementMapResponseTypes(FilterProcessor processor) throws ReportingServiceException {
        try {
            String request = ExtMovementMessageMapper.mapToGetMovementMapByQueryRequest(processor.toMovementQuery());
            String moduleMessage = movementSender.sendModuleMessage(request, receiver.getDestination());
            TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
            if (response != null && !isUserFault(response)) {
                return ExtMovementMessageMapper.mapToMovementMapResponse(response);
            } else {
                throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT");
            }
        } catch (JMSException | ModelMapperException | MovementModelException | MessageException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT", e);
        }
    }
    private List<MovementMapResponseType> getMovementMapResponseTypesReporting(FilterProcessor processor) throws ReportingServiceException {
    	try {
    		String request = ExtMovementMessageMapper.mapToGetMovementMapByQueryRequestReporting(processor.toMovementQuery());
    		String moduleMessage = movementSender.sendModuleMessage(request, receiver.getDestination());
    		TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
    		if (response != null && !isUserFault(response)) {
    			return ExtMovementMessageMapper.mapToMovementMapResponse(response);
    		} else {
    			throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT");
    		}
    	} catch (JMSException | ModelMapperException | MovementModelException | MessageException e) {
    		throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT", e);
    	}
    }

    private boolean isUserFault(TextMessage message) {
        boolean isErrorResponse = false;
        try {
            UserFault userFault = JAXBMarshaller.unmarshall(message, UserFault.class);
            log.error("UserFault error JMS message received with text: " + userFault.getFault());
            isErrorResponse = true;
        } catch (ReportingModelException e) {
            //do nothing  since it's not a UserFault
            log.trace("Unexpected exception was thrown.", e);
        }
        return isErrorResponse;
    }

}