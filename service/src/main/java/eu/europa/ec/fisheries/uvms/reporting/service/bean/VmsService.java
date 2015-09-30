package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;

public interface VmsService {

    VmsDTO getVmsDataByReportId(String username, Long id) throws ServiceException;

    VmsDTO getVmsMockData(Long id);

}
