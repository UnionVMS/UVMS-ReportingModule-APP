package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

/**
 * //TODO create test
 */
@LocalBean
@Stateless
public class MovementServiceBean {

    @EJB
    private MovementModuleSenderBean movementSender;

    @EJB
    private MovementModuleReceiverBean movementReceiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<MovementMapResponseType> getMovementMapResponseTypes(FilterProcessor processor) throws ReportingServiceException {

        try {

            String movementMapByQueryRequest = ExtendedMovementMessageMapper.mapToGetMovementMapByQueryRequest(processor.toMovementQuery());
            String moduleMessage = movementSender.sendModuleMessage(movementMapByQueryRequest, movementReceiver.getDestination());
            TextMessage response = movementReceiver.getMessage(moduleMessage, TextMessage.class);
            return ExtendedMovementMessageMapper.mapToMovementMapResponse(response);

        } catch (ModelMapperException | JMSException | MessageException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM SPATIAL");
        }
    }
}
