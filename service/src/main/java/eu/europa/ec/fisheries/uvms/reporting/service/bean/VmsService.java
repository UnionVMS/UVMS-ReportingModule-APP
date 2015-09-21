package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;

import java.util.Set;

public interface VmsService {

    VmsDto getVmsDataByReportId(Long id);

    VmsDto getVmsMockData(Long id);

}
