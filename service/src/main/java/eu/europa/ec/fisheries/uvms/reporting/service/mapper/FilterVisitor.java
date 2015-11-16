package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;

public interface FilterVisitor<T> {

    T visitVmsTrackFilter(VmsTrackFilter trackFilter);
    T visitVmsSegmentFilter(VmsSegmentFilter segmentFilter);
    T visitVmsPositionFilter(VmsPositionFilter positionFilter);
    T visitVesselFilter(VesselFilter vesselFilter);
    T visitVesselGroupFilter(VesselGroupFilter vesselGroupFilter);
    T visitAreaFilter(AreaFilter areaFilter);
    T visitCommonFilter(CommonFilter commonFilter);

}
