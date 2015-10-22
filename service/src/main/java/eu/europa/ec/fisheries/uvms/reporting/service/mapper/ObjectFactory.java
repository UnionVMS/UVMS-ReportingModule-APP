package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;

public class ObjectFactory {

    public Report createReport() {
        return Report.ReportBuilder().build();
    }

    public CommonFilter createCommonFilter() {
        return CommonFilter.CommonFilterBuilder().build();
    }

    public ReportDTO createReportDTO() {
        return new ReportDTO();
    }

    public PositionSelector createPositionSelector() {
        return PositionSelector.PositionSelectorBuilder().build();
    }

    public VesselFilterDTO createVesselFilterDTO() {
        return new VesselFilterDTO();
    }

    public VmsPositionFilterDTO createVmsPositionFilterDTO() {
        return new VmsPositionFilterDTO();
    }

    public VesselGroupFilterDTO createVesselGroupFilterDTO() {
        return new VesselGroupFilterDTO();
    }

    public CommonFilterDTO createCommonFilterDTO(){
        return new CommonFilterDTO();
    }

    public PositionSelectorDTO createPositionSelectorDTO(){
        return new PositionSelectorDTO();
    }

    public VesselFilter createVesselFilter(){
        return VesselFilter.VesselFilterBuilder().build();
    }

    public VmsTrackFilter createTrackFilter(){
        return VmsTrackFilter.TrackFilterBuilder().build();
    }

    public TrackFilterDTO createTrackFilterDTO(){
        return new TrackFilterDTO();
    }

    public VmsPositionFilter createVmsPositionFilter(){
        return  VmsPositionFilter.VmsPositionFilterBuilder().build();
    }

    public VmsSegmentFilter createVmsSegmentFilter(){
        return VmsSegmentFilter.VmsSegmentFilterBuilder().build();
    }

    public VmsSegmentFilterDTO createVmsSegmentFilterDTO(){
        return new VmsSegmentFilterDTO();
    }


}
