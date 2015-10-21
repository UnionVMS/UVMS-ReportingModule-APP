package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

/**
 * //TODO create test
 */
@Stateless
@LocalBean
public class MovementModuleSenderBean extends AbstractProducer {

    @Resource(mappedName = MessageConstants.QUEUE_MODULE_MOVEMENT)
    private Destination destination;

    @Override
    protected Destination getDestination() {
        return destination;
    }

}
