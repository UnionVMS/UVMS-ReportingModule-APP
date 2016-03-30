package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.vms.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

public interface VmsService {

    VmsDTO getVmsDataByReportId(String username, String scopeName, Long id, List<AreaIdentifierType> areaRestrictions, DateTime now, Boolean isAdmin) throws ReportingServiceException;

    VmsDTO getVmsDataBy(Report report, List<AreaIdentifierType> areaRestrictions) throws ReportingServiceException;

}
