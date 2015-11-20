package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

@Stateless
public class MovementServiceBean {

    @EJB
    private MovementModuleSenderBean movementSender;

    @EJB
    private MovementModuleReceiverBean movementReceiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Map<String, MovementMapResponseType> getMovementMap(FilterProcessor processor) throws ReportingServiceException {

        return ExtMovementMessageMapper.getMovementMap(getMovementMapResponseTypes(processor));

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<MovementMapResponseType> getMovement(FilterProcessor processor) throws ReportingServiceException {

        return getMovementMapResponseTypes(processor);
    }

    private List<MovementMapResponseType> getMovementMapResponseTypes(FilterProcessor processor) throws ReportingServiceException {

        try {

            String request = ExtMovementMessageMapper.mapToGetMovementMapByQueryRequest(processor.toMovementQuery());
            String moduleMessage = movementSender.sendModuleMessage(request, movementReceiver.getDestination());
            TextMessage response = movementReceiver.getMessage(moduleMessage, TextMessage.class);
            return ExtMovementMessageMapper.mapToMovementMapResponse(response);

        } catch (ModelMapperException | JMSException | MessageException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT", e);
        }
    }
}
