package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;

import org.apache.commons.lang3.NotImplementedException;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialMessageService;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;

@Stateless
@Local(SpatialService.class)
public class SpatialServiceBean implements SpatialService {

	@EJB
	SpatialMessageService spatialMessageService;
	
	@Override
	public String getFilterArea(List<AreaIdentifierType> userAreas) throws ReportingServiceException {
		try {
			return spatialMessageService.getFilteredAreas(userAreas);
		} catch (SpatialModelMapperException | MessageException | JMSException e) {
			throw new ReportingServiceException(e);
		}
	}

	@Override
	public String getFilterArea(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws ReportingServiceException {
		throw new NotImplementedException("Not implemented");
	}

}
