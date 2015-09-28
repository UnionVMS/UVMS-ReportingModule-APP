package eu.europa.ec.fisheries.uvms.reporting.service.visitor;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.PositionFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;

public class DTOToFilterVisitor implements FilterDTOVisitor<Filter> {

    @Override
    public Filter visitPositionFilterDTO(PositionFilterDTO positionFilterDTO) {
        return PositionFilterMapper.INSTANCE.positionFilterDTOToPositionFilter(positionFilterDTO);
    }

    @Override
    public Filter visitVesselFilterDTO(VesselFilterDTO vesselFilterDTO) {
        return VesselFilterMapper.INSTANCE.vesselFilterDTOToVesselFilter(vesselFilterDTO);
    }

    @Override
    public Filter visitVesselGroupFilterDTO(VesselGroupFilterDTO vesselGroupFilterDTO) {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterDTOToVesselGroupFilter(vesselGroupFilterDTO);
    }

    @Override
    public Filter visitVmsPositionFilterDTO(VmsPositionFilterDTO vmsPositionFilterDTO) {
        return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterDTOToVmsPositionFilter(vmsPositionFilterDTO);
    }
}
