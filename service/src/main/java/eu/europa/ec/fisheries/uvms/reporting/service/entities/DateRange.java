package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
@EqualsAndHashCode
public class DateRange implements Serializable {

    public static final String END_DATE = "end_date";
    public static final String START_DATE = "start_date";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = START_DATE)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = END_DATE)
    private Date endDate;

    public DateRange() {
    }

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        Date startDate = null;
        if (this.startDate != null) {
            startDate = new Date(this.startDate.getTime());
        }
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            this.startDate = new Date(startDate.getTime());
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        Date endDate = null;
        if (this.endDate != null) {
            endDate = new Date(this.endDate.getTime());
        }
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null) {
            this.endDate = new Date(endDate.getTime());
        }
        this.endDate = endDate;
    }
}
