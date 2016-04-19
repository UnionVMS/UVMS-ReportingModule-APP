package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;

public class ObjectFactory {

    public Audit createAudit() {
        return Audit.builder().build();
    }

    public Report createReport() {
        return Report.builder().build();
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

    public AssetFilterDTO createAssetFilterDTO() {
        return new AssetFilterDTO();
    }

    public VmsPositionFilterDTO createVmsPositionFilterDTO() {
        return new VmsPositionFilterDTO();
    }

    public AssetGroupFilterDTO createAssetGroupFilterDTO() {
        return new AssetGroupFilterDTO();
    }

    public CommonFilterDTO createCommonFilterDTO(){
        return new CommonFilterDTO();
    }

    public PositionSelectorDTO createPositionSelectorDTO(){
        return new PositionSelectorDTO();
    }

    public AssetFilter createAssetFilter(){
        return AssetFilter.builder().build();
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
