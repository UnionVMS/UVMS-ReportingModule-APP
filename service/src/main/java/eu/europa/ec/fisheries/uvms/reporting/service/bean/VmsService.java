package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;

import java.util.Set;

public interface VmsService {

    VmsDto getVmsData(Set<Integer> vesselIds);

    VmsDto getVmsMockData(Set<Integer> vesselIds);

}
