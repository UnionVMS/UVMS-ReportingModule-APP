package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.CommonFilterMapper;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true, of = {"startDate", "endDate"})
public class CommonFilterDTO extends FilterDTO {

    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String POSITION_SELECTOR = "positionSelector";
    public static final String COMMON = "common";

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date startDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date endDate;

    private PositionSelectorDTO positionSelector;

    public CommonFilterDTO() {
        super(FilterType.common);
    }

    public CommonFilterDTO(Long id, Long reportId) {
        super(FilterType.common, id, reportId);
    }

    @Builder(builderMethodName = "CommonFilterDTOBuilder")
    public CommonFilterDTO(Long reportId, Long id, Date startDate, Date endDate, PositionSelectorDTO positionSelector) {
        this(id, reportId);
        this.startDate = startDate;
        this.endDate = endDate;
        this.positionSelector = positionSelector;
        validate();
    }

    @Override
    public Filter convertToFilter() {
        return CommonFilterMapper.INSTANCE.dateTimeFilterDTOToDateTimeFilter(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PositionSelectorDTO getPositionSelector() {
        return positionSelector;
    }

    public void setPositionSelector(PositionSelectorDTO positionSelector) {
        this.positionSelector = positionSelector;
    }
}
