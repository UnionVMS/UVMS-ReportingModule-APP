package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;

public class ObjectFactory {

    public Report createReport() {
        return Report.ReportBuilder().build();
    }

    public CommonFilter createCommonFilter() {
        return CommonFilter.builder().build();
    }

    public ReportDTO createReportDTO() {
        return new ReportDTO();
    }

    public PositionSelector createPositionSelector() {
        return PositionSelector.builder().build();
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
        return VesselFilter.builder().build();
    }

    public VmsTrackFilter createTrackFilter(){
        return VmsTrackFilter.builder().build();
    }

    public TrackFilterDTO createTrackFilterDTO(){
        return new TrackFilterDTO();
    }

    public VmsPositionFilter createVmsPositionFilter(){
        return  VmsPositionFilter.builder().build();
    }

    public VmsSegmentFilter createVmsSegmentFilter(){
        return VmsSegmentFilter.builder().build();
    }

    public VmsSegmentFilterDTO createVmsSegmentFilterDTO(){
        return new VmsSegmentFilterDTO();
    }


}
