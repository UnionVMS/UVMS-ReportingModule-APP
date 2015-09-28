package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterDTOVisitor;

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
    public <T> T accept(FilterDTOVisitor<T> visitor) {
        return visitor.visitPositionFilterDTO(this);
    }
}
