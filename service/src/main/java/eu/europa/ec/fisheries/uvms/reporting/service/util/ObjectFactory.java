/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.util;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.VmsSegmentFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.VmsTrackFilter;

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