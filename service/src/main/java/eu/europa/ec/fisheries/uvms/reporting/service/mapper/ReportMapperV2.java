/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ObjectFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(imports = {ReportDetails.class, Report.class, DateUtils.class, Audit.class, VisibilityEnum.class}, uses = {ObjectFactory.class})
public abstract class ReportMapperV2 {

    public static final ReportMapperV2 INSTANCE = Mappers.getMapper(ReportMapperV2.class);

    @Mappings({
            @Mapping(target = "visibility", expression = "java(VisibilityEnum.getByName(dto.getVisibility()))"),
            @Mapping(target = "audit", expression = "java(new Audit(dto.getCreatedOn() != null ? DateUtils.UI_FORMATTER.parseDateTime(dto.getCreatedOn()).toDate() : null))"),
            @Mapping(target = "details", expression = "java(new ReportDetails(dto.getDesc(), dto.getName(), dto.isWithMap(), null, dto.getCreatedBy()))"),
            @Mapping(target = "filters", expression = "java(mapFilters(dto))"),
    })
    public abstract Report reportDtoToReport(eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report dto);

    public abstract eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report reportToReportDto(final Report entity); // TODO test if same as in mapper V1

    Set<Filter> mapFilters(eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report dto){

        Set<Filter> filterSet = new HashSet<>();

        filterSet.addAll(AreaFilterMapper.INSTANCE.arealistToAreaFilterSet(dto.getFilterExpression().getAreas()));
        filterSet.add(CommonFilterMapper.INSTANCE.commonToCommonFilter(dto.getFilterExpression().getCommon()));

        List<Asset> assets = dto.getFilterExpression().getAssets();

        for (Asset asset : assets){
            if ("vgroup".equals(asset.getType())){
                filterSet.add(AssetGroupFilterMapper.INSTANCE.assetToAssetFilterGroup(asset));
            }
            else if ("asset".equals(asset.getType())){
                filterSet.add(AssetFilterMapper.INSTANCE.assetToAssetFilter(asset));
            }
        }

        if (dto.getFilterExpression().getVms() != null){
            filterSet.add(VmsTrackFilterMapper.INSTANCE.tracksToVmsTrackFilter(dto.getFilterExpression().getVms().getVmstrack()));
            filterSet.add(VmsPositionFilterMapper.INSTANCE.vmsPositionToVmsPositionFilter(dto.getFilterExpression().getVms().getVmsposition()));
            filterSet.add(VmsSegmentFilterMapper.INSTANCE.vmsSegmentToVmsSegmentFilter(dto.getFilterExpression().getVms().getVmssegment()));
        }

        if (dto.getFilterExpression().getFaFilter() != null) {
            filterSet.add(FaFilterMapper.INSTANCE.faFilterDtoToFaFilter(dto.getFilterExpression().getFaFilter()));
        }

        if (dto.getFilterExpression().getCriteriaFilter() != null) {
            filterSet.add(GroupCriteriaFilterMapper.INSTANCE.mapCriteriaFilterDTOToCriteriaFilter(dto.getFilterExpression().getCriteriaFilter()));
        }

        return filterSet;
    }
}