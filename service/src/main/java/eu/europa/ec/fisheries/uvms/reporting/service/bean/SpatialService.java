package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;

public interface SpatialService {
	
	String getFilterArea(List<AreaIdentifierType> userAreas) throws ReportingServiceException;
	
	String getFilterArea(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws ReportingServiceException;

}
