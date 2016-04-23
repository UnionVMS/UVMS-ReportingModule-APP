package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@ToString
public class DistanceRange implements Serializable {

    @Column(name = "MIN_DISTANCE")
    private Float minDistance;

    @Column(name = "MAX_DISTANCE")
    private Float maxDistance;

    public DistanceRange() {
    }

    public DistanceRange(Float minDistance, Float maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    public Float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(Float minDistance) {
        this.minDistance = minDistance;
    }

    public Float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Float maxDistance) {
        this.maxDistance = maxDistance;
    }
}
