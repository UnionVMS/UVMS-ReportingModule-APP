package eu.europa.ec.fisheries.uvms.reporting.message.service;

import java.util.List;

import javax.jms.JMSException;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.message.MessageService;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;

public interface SpatialMessageService extends MessageService  {
	
    String getFilteredAreas(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws MessageException, SpatialModelMapperException, JMSException;
    
    String getFilteredAreas(List<AreaIdentifierType> userAreas) throws MessageException, SpatialModelMapperException, JMSException;
}
