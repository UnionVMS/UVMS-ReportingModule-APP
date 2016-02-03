package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.vms.Common;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
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
    CommonFilterDTO dateTimeFilterToDateTimeFilterDTO(CommonFilter commonFilter); //TODO use Common from model

    @Mappings({
            @Mapping(target = "dateRange", expression = "java(new DateRange(dto.getStartDate(), dto.getEndDate()))")
    })
    CommonFilter dateTimeFilterDTOToDateTimeFilter(CommonFilterDTO dto); //TODO use Common from model

    @Mappings({
            @Mapping(target = "dateRange", expression = "java(new DateRange(DateUtils.UI_FORMATTER.parseDateTime(dto.getStartDate()).toDate(), DateUtils.UI_FORMATTER.parseDateTime(dto.getEndDate()).toDate()))"),
            @Mapping(target = "positionSelector", expression = "java(new PositionSelector(Float.valueOf(dto.getXValue()), Enum.valueOf( Selector.class, dto.getPositionSelector()) , Position.getByName(dto.getPositionTypeSelector())))")
    })
    CommonFilter commonToCommonFilter(Common dto);

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
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(CommonFilter incoming, @MappingTarget CommonFilter current);
}
