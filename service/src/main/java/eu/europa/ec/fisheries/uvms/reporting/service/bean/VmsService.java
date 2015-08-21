package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;

import java.util.Set;

public interface VmsService {

    VmsDto getMonitoringData(Set<Integer> vesselIds);

    VmsDto getMonitoringMockData(Set<Integer> vesselIds);
}
