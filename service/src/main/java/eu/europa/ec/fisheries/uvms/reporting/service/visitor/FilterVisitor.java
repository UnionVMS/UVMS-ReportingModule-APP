package eu.europa.ec.fisheries.uvms.reporting.service.visitor;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;

public interface FilterVisitor<T> {

    T visitPositionFilter(PositionFilter positionFilter);
    T visitVesselFilter(VesselFilter vesselFilter);
    T visitVesselGroupFilter(VesselGroupFilter vesselGroupFilter);
    T visitVmsPositionFilter(VmsPositionFilter vmsPositionFilter);
}
