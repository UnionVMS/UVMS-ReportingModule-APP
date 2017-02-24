/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.domain.DateRange;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DateTime;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ObjectFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.Date;

@Mapper(uses = {ObjectFactory.class, PositionSelectorMapper.class}, imports = {DateUtils.class, Selector.class, Position.class, DateRange.class, PositionSelector.class})
public interface CommonFilterMapper {

    CommonFilterMapper INSTANCE = Mappers.getMapper(CommonFilterMapper.class);

    @Mappings({
            @Mapping(source = "dateRange.startDate", target = "startDate"),
            @Mapping(source = "dateRange.endDate", target = "endDate")
    })
    CommonFilterDTO dateTimeFilterToDateTimeFilterDTO(CommonFilter commonFilter);

    @Mappings({
            @Mapping(target = "dateRange", expression = "java(new DateRange(dto.getStartDate(), dto.getEndDate()))")
    })
    CommonFilter dateTimeFilterDTOToDateTimeFilter(CommonFilterDTO dto);

    @Mappings({
            @Mapping(target = "dateRange", expression = "java(new DateRange(dto.getStartDate() != null ? DateUtils.UI_FORMATTER.parseDateTime(dto.getStartDate()).toDate() : null, " +
                    "dto.getEndDate() != null ? DateUtils.UI_FORMATTER.parseDateTime(dto.getEndDate()).toDate() : null))"),
            @Mapping(target = "positionSelector", expression = "java(new PositionSelector(dto.getXValue() != null ? Float.valueOf(dto.getXValue()) : null, Enum.valueOf( Selector.class, dto.getPositionSelector()) , Position.getByName(dto.getPositionTypeSelector())))")
    })
    CommonFilter commonToCommonFilter(DateTime dto);

    @Mappings({
            @Mapping(constant = "DATE", target = "key"),
            @Mapping(source = "dateRange.startDate", target = "from", dateFormat = "yyyy-MM-dd HH:mm:ss Z"),
            @Mapping(source = "dateRange.endDate", target = "to", dateFormat = "yyyy-MM-dd HH:mm:ss Z")
    })
    RangeCriteria dateRangeToRangeCriteria(CommonFilter commonFilter);

    @Mappings({
            @Mapping(constant = "DATE", target = "key"),
            @Mapping(source = "startDate", target = "from", dateFormat = "yyyy-MM-dd HH:mm:ss Z"),
            @Mapping(source = "endDate", target = "to", dateFormat = "yyyy-MM-dd HH:mm:ss Z")
    })
    RangeCriteria dateRangeToRangeCriteria(Date startDate, Date endDate);

    @Mappings({
            @Mapping(constant = "NR_OF_LATEST_REPORTS", target = "key"),
            @Mapping(target = "value",
                    expression = "java(String.valueOf(Math.round(filter.getPositionSelector().getValue())))")
    })
    ListCriteria positionToListCriteria(CommonFilter filter);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(CommonFilter incoming, @MappingTarget CommonFilter current);
}