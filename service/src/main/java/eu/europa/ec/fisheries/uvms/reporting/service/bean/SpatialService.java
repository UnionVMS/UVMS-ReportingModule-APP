package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;

import java.util.List;

public interface SpatialService {

    String getFilterArea(List<AreaIdentifierType> userAreas) throws ReportingServiceException;

    String getFilterArea(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws ReportingServiceException;

    SpatialSaveOrUpdateMapConfigurationRS saveMapConfiguration(Long id, MapConfigurationDTO mapConfiguration) throws ReportingServiceException;
}
