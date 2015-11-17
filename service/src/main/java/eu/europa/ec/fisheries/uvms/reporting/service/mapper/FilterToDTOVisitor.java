package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;

public class FilterToDTOVisitor implements FilterVisitor<FilterDTO> {

    @Override
    public FilterDTO visitVmsTrackFilter(VmsTrackFilter trackFilter) {
        return TrackFilterMapper.INSTANCE.trackFilterToTrackFilterDTO(trackFilter);
    }

    @Override
    public FilterDTO visitVmsSegmentFilter(VmsSegmentFilter segmentFilter) {
        return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterToVmsSegmentFilterDTO(segmentFilter);
    }

    @Override
    public FilterDTO visitVmsPositionFilter(VmsPositionFilter positionFilter) {
        return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterToVmsPositionFilterDTO(positionFilter);
    }

    @Override
    public FilterDTO visitVesselFilter(VesselFilter vesselFilter) {
        return VesselFilterMapper.INSTANCE.vesselFilterToVesselFilterDTO(vesselFilter);
    }

    @Override
    public FilterDTO visitVesselGroupFilter(VesselGroupFilter vesselGroupFilter) {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterToVesselGroupFilterDTO(vesselGroupFilter);
    }

    @Override
    public FilterDTO visitAreaFilter(AreaFilter areaFilter) {
        return AreaFilterMapper.INSTANCE.areaFilterToAreaFilterDTO(areaFilter);
    }

    @Override
    public FilterDTO visitCommonFilter(CommonFilter commonFilter) {
        return DateTimeFilterMapper.INSTANCE.dateTimeFilterToDateTimeFilterDTO(commonFilter);
    }


}
