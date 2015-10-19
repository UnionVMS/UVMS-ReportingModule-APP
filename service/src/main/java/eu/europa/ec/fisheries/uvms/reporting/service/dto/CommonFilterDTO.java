package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.DateTimeFilterMapper;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.NotImplementedException;

import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true, of = {"startDate", "endDate"})
public class CommonFilterDTO extends FilterDTO {

    public final static String START_DATE = "startDate";
    public final static String END_DATE = "endDate";
    public final static String POSITION_SELECTOR = "positionSelector";
    public final static String COMMON = "common";

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date startDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date endDate;

    @Builder(builderMethodName = "CommonFilterDTOBuilder")
    public CommonFilterDTO(Long reportId, Long id, Date startDate, Date endDate, PositionSelectorDTO positionSelector) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.positionSelector = positionSelector;
        setId(id);
        setReportId(reportId);
        setType(FilterType.common);
        validate();
    }

    private PositionSelectorDTO positionSelector;

    public CommonFilterDTO() {

    }

    @Override
    public Filter convertToFilter() {
        return DateTimeFilterMapper.INSTANCE.dateTimeFilterDTOToDateTimeFilter(this);
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
