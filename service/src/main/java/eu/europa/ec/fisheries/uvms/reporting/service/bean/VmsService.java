package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;

public interface VmsService {

    VmsDTO getVmsDataByReportId(String username, String scopeName, Long id) throws ReportingServiceException;

}
