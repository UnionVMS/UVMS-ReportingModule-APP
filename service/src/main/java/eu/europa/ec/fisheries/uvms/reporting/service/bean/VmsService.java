package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;

public interface VmsService {

    VmsDTO getVmsDataByReportId(Long id);

    VmsDTO getVmsMockData(Long id);

}
