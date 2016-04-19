package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class TimeRange implements Serializable {

    private @Column(name = "MIN_TIME") Float minTime;
    private @Column(name = "MAX_TIME") Float maxTime;

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
