package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;

import static eu.europa.ec.fisheries.uvms.reporting.model.constants.ModuleConstants.*;
import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.List;

/**
 * //TODO create test
 */
@Stateless
@LocalBean
public class MovementMessageServiceBean extends AbstractMessageService {

    @Resource(mappedName = COMPONENT_RESPONSE_QUEUE)
    private Queue response;

    @Resource(mappedName = QUEUE_MODULE_MOVEMENT)
    private Destination request;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @PostConstruct
    public void init(){

    }

    public List<MovementMapResponseType> getMovementMap(MovementQuery movementQuery) throws JMSException, MessageException, eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException {
        String movementRequestString = MovementModuleRequestMapper.mapToGetMovementMapByQueryRequest(movementQuery);
        String movementMessageId = sendModuleMessage(movementRequestString);
        TextMessage movementReceiverMessage = getMessage(movementMessageId, TextMessage.class);
        return MovementModuleResponseMapper.mapToMovementMapResponse(movementReceiverMessage);
    }

    @Override
    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    protected Destination getEventDestination() {
        return request;
    }

    @Override
    protected Destination getResponseDestination() {
        return response;
    }

    @Override
    protected String getModuleName() {
        return MODULE_NAME;
    }

}
