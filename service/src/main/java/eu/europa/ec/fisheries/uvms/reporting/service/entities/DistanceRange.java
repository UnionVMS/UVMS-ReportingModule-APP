package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@ToString
public class DistanceRange implements Serializable {

    private @Column(name = "MIN_DISTANCE") Float minDistance;
    private @Column(name = "MAX_DISTANCE") Float maxDistance;

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
