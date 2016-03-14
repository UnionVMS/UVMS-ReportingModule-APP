package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.message.AbstractJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.user.types.UserFault;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;

@Stateless
@Slf4j
public class MovementServiceBean  extends AbstractJAXBMarshaller {

    @EJB
    private MovementModuleSenderBean movementSender;

    @EJB
    private ReportingModuleReceiverBean receiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Map<String, MovementMapResponseType> getMovementMap(FilterProcessor processor) throws ReportingServiceException {

        return ExtMovementMessageMapper.getMovementMap(getMovementMapResponseTypes(processor));

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<MovementMapResponseType> getMovement(FilterProcessor processor) throws ReportingServiceException {
        log.debug("getMovement({})", processor.toString());
        return getMovementMapResponseTypes(processor);
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

        } catch (ModelMapperException | JMSException | MessageException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT", e);
        }
    }

    private boolean isUserFault(TextMessage message) {
        boolean isErrorResponse = false;

        try {
            UserFault userFault = unmarshallTextMessage(message, UserFault.class);
            log.error("UserFault error JMS message received with text: " + userFault.getFault());
            isErrorResponse = true;
        } catch (JAXBException | JMSException e) {
            //do nothing  since it's not a UserFault
        }

        return isErrorResponse;
    }
}
