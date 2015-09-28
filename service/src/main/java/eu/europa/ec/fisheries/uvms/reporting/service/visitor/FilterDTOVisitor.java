package eu.europa.ec.fisheries.uvms.reporting.service.visitor;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;

public interface FilterDTOVisitor<T> {

    T visitPositionFilterDTO(PositionFilterDTO positionFilterDTO);
    T visitVesselFilterDTO(VesselFilterDTO vesselFilterDTO);
    T visitVesselGroupFilterDTO(VesselGroupFilterDTO vesselGroupFilterDTO);
    T visitVmsPositionFilterDTO(VmsPositionFilterDTO vmsPositionFilterDTO);

}
