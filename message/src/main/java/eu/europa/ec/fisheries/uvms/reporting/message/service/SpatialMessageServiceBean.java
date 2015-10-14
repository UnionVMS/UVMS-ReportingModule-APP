package eu.europa.ec.fisheries.uvms.reporting.message.service;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.CONNECTION_FACTORY;
import static eu.europa.ec.fisheries.uvms.message.MessageConstants.QUEUE_MODULE_SPATIAL;
import static eu.europa.ec.fisheries.uvms.message.MessageConstants.QUEUE_REPORTING;
import static eu.europa.ec.fisheries.uvms.reporting.model.constants.ModuleConstants.MODULE_NAME;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;

@Stateless
@Local(SpatialMessageService.class)
public class SpatialMessageServiceBean extends AbstractMessageService implements SpatialMessageService {

    @Resource(mappedName = QUEUE_MODULE_SPATIAL)
    private Destination request;
    
    @Resource(mappedName = QUEUE_REPORTING)
    private Queue response;
    
    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private SpatialModuleRequestMapper requestMapper;
    
    private SpatialModuleResponseMapper responseMapper;

    @PostConstruct
    public void init() {
    	requestMapper = new SpatialModuleRequestMapper();
    	responseMapper = new SpatialModuleResponseMapper();
    }
    
    public String getFilteredAreas(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws MessageException, SpatialModelMapperException, JMSException {
    	String requestString = requestMapper.mapToFilterAreaSpatialRequest(scopeAreas, userAreas);
    	String messageId = sendModuleMessage(requestString);
    	TextMessage receiverMessage = getMessage(messageId, TextMessage.class);
    	return responseMapper.mapToFilterAreasSpatialRSFromResponse(receiverMessage, receiverMessage.getJMSCorrelationID());    	
    }
    
    public String getFilteredAreas(List<AreaIdentifierType> userAreas) throws MessageException, SpatialModelMapperException, JMSException {
    	return getFilteredAreas(Collections.<AreaIdentifierType>emptyList(), userAreas);
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
