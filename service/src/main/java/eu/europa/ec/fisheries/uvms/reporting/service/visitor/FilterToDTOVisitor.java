package eu.europa.ec.fisheries.uvms.reporting.service.visitor;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.DateTimeFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;

public class FilterToDTOVisitor implements FilterVisitor<FilterDTO> {

    @Override
    public FilterDTO visitPositionFilter(final DateTimeFilter positionFilter) {
        return DateTimeFilterMapper.INSTANCE.positionFilterToPositionFilterDTO(positionFilter);
    }

    @Override
    public FilterDTO visitVesselFilter(final VesselFilter vesselFilter) {
        return VesselFilterMapper.INSTANCE.vesselFilterToVesselFilterDTO(vesselFilter);
    }

    @Override
    public FilterDTO visitVesselGroupFilter(final VesselGroupFilter vesselGroupFilter) {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterToVesselGroupFilterDTO(vesselGroupFilter);
    }

    @Override
    public FilterDTO visitVmsPositionFilter(final VmsPositionFilter vmsPositionFilter) {
        return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterToVmsPositionFilterDTO(vmsPositionFilter);
    }

}
