package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class TimeRange implements Serializable {

    @Column(name = "MIN_TIME")
    private Float minTime;

    @Column(name = "MAX_TIME")
    private Float maxTime;

    public TimeRange() {
    }

    public TimeRange(Float minTime, Float maxTime) {
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public Float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Float maxTime) {
        this.maxTime = maxTime;
    }

    public Float getMinTime() {
        return minTime;
    }

    public void setMinTime(Float minTime) {
        this.minTime = minTime;
    }

}
