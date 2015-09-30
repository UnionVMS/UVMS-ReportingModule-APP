package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.DateTimeFilterMapper;

import java.util.Date;

@JsonTypeName("position")
public class PositionFilterDTO extends FilterDTO {

    private Date startDate;
    private Date endDate;

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

    @Override
    public Filter convertToFilter() {
        return DateTimeFilterMapper.INSTANCE.positionFilterDTOToPositionFilter(this);
    }
}
