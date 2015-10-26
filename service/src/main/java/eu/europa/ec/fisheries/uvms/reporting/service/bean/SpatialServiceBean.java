package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.commons.lang3.NotImplementedException;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;

@Stateless
@Local(SpatialService.class)
public class SpatialServiceBean implements SpatialService {
	
	@EJB
	private SpatialProducerBean spatialProducerBean;
	
	@EJB
	private SpatialConsumerBean spatialConsumerBean;
	
	@Override
	public String getFilterArea(List<AreaIdentifierType> userAreas) throws ReportingServiceException {
		try {
			SpatialModuleRequestMapper requestMapper = new SpatialModuleRequestMapper();
			String requestMsg = SpatialModuleRequestMapper.mapToFilterAreaSpatialRequest(Collections.<AreaIdentifierType>emptyList(), userAreas);
			String correlationId = spatialProducerBean.sendModuleMessage(requestMsg, spatialConsumerBean.getDestination());
			Message message = spatialConsumerBean.getMessage(correlationId, TextMessage.class);
			return getText(message);
		} catch (SpatialModelMapperException | MessageException | JMSException e) {
			throw new ReportingServiceException(e);
		}
	}

	@Override
	public String getFilterArea(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws ReportingServiceException {
		throw new NotImplementedException("Not implemented");
	}
	
	private String getText(Message message) throws JMSException {
		if(message instanceof TextMessage) {
			return ((TextMessage)message).getText();
		}
		return null;
	}

}
