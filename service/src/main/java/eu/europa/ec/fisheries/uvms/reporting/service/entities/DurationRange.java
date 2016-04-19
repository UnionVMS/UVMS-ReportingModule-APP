package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@ToString
public class DurationRange implements Serializable {

    private @Column(name = "MIN_DURATION") Float minDuration;
    private @Column(name = "MAX_DURATION") Float maxDuration;

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
