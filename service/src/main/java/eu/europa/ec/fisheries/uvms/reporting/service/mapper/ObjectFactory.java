package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;

public class ObjectFactory {

    public Report createReport() {
        return Report.ReportBuilder().build();
    }

    public CommonFilter createPositionFilter() {
        return CommonFilter.DateTimeFilterBuilder().build();
    }

    public ReportDTO createReportDTO() {
        return ReportDTO.ReportDTOBuilder().build();
    }

    public PositionSelector createPositionSelector() {
        return PositionSelector.PositionSelectorBuilder().build();
    }

    public VesselFilterDTO createVesselFilterDTO() {
        return VesselFilterDTO.VesselFilterDTOBuilder().build();
    }

    public VmsPositionFilterDTO createVmsPositionFilterDTO() {
        return VmsPositionFilterDTO.VmsPositionFilterDTOBuilder().build();
    }

    public VesselGroupFilterDTO createVesselGroupFilterDTO() {
        return VesselGroupFilterDTO.VesselGroupFilterDTOBuilder().build();
    }

    public CommonFilterDTO createCommonFilterDTO(){
        return CommonFilterDTO.CommonFilterDTOBuilder().build();
    }

    public PositionSelectorDTO createPositionSelectorDTO(){
        return PositionSelectorDTO.PositionSelectorDTOBuilder().build();
    }

    VesselFilter createVesselFilter(){
        return VesselFilter.VesselFilterBuilder().build();
    }

}
