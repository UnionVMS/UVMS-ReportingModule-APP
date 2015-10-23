package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;

public interface VmsService {

    VmsDTO getVmsDataByReportId(String username, String scopeName, Long id) throws ReportingServiceException, MessageException, VesselModelMapperException;

    VmsDTO getVmsMockData(Long id) throws ReportingServiceException;

}
