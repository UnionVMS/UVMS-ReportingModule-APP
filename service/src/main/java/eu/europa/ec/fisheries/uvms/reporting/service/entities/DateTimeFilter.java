package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.DateTimeFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue("DATETIME")
@EqualsAndHashCode(callSuper = true)
public class DateTimeFilter extends Filter {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    @Embedded
    private PositionSelector positionSelector;

    DateTimeFilter(){
        super(FilterType.DATETIME);
    }

    @Builder
    public DateTimeFilter(Date startDate, Date endDate) {
        super(FilterType.DATETIME);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public FilterDTO convertToDTO() {
        return DateTimeFilterMapper.INSTANCE.dateTimeFilterToDateTimeFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        DateTimeFilter incoming = (DateTimeFilter) filter;
        this.setEndDate(incoming.getEndDate());
        this.setStartDate(incoming.getStartDate());
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

    public PositionSelector getPositionSelector() {
        return positionSelector;
    }

    public void setPositionSelector(PositionSelector positionSelector) {
        this.positionSelector = positionSelector;
    }

}
