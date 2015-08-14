package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.MonitoringDto;

import java.util.Set;

public interface MonitoringService {

    MonitoringDto getMovements(Set<Integer> vesselIds);
}
