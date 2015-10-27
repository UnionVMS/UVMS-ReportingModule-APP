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
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingJMSConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;

@Stateless
@Local(SpatialService.class)
public class SpatialServiceBean implements SpatialService {
	
	@EJB
	private SpatialProducerBean spatialProducerBean;
	
	@EJB
	private ReportingJMSConsumerBean reportingJMSConsumerBean;
	
	@Override
	public String getFilterArea(List<AreaIdentifierType> userAreas) throws ReportingServiceException {
		try {
			String correlationId = spatialProducerBean.sendModuleMessage(getRequest(userAreas), reportingJMSConsumerBean.getDestination());
			Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
			return getResponse(message, correlationId);
		} catch (SpatialModelMapperException | MessageException | JMSException e) {
			throw new ReportingServiceException(e);
		}
	}

	@Override
	public String getFilterArea(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws ReportingServiceException {
		throw new NotImplementedException("Not implemented");
	}
	
	private String getRequest(List<AreaIdentifierType> userAreas) throws SpatialModelMarshallException {
		return SpatialModuleRequestMapper.mapToFilterAreaSpatialRequest(Collections.<AreaIdentifierType>emptyList(), userAreas);
	}
	
	private String getResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
		FilterAreasSpatialRS filterAreaSpatialResponse =  SpatialModuleResponseMapper.mapToFilterAreasSpatialRSFromResponse(getText(message), correlationId);
		return filterAreaSpatialResponse.getGeometry();
	}
	
	private TextMessage getText(Message message) throws JMSException {
		if(message instanceof TextMessage) {
			return (TextMessage)message;
		}
		return null;
	}
}
