package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;

import java.util.List;
import java.util.Set;

public interface SpatialService {

    String getFilterArea(Set<AreaIdentifierType> scopeAreas, Set<AreaIdentifierType> userAreas) throws ReportingServiceException;

    MapConfigurationDTO getMapConfiguration(long reportId) throws ReportingServiceException;

    boolean saveOrUpdateMapConfiguration(long reportId, MapConfigurationDTO mapConfiguration) throws ReportingServiceException;

    void deleteMapConfiguration(List<Long> spatialConnectIds) throws ReportingServiceException;
}
