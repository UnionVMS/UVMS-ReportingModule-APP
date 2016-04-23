package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@ToString
public class DurationRange implements Serializable {

    @Column(name = "MIN_DURATION")
    private Float minDuration;

    @Column(name = "MAX_DURATION")
    private Float maxDuration;

    public DurationRange() {
    }

    public DurationRange(Float minDuration, Float maxDuration) {
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

    public Float getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Float minDuration) {
        this.minDuration = minDuration;
    }

    public Float getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Float maxDuration) {
        this.maxDuration = maxDuration;
    }
}
