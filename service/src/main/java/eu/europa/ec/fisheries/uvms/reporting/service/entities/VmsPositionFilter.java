package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterVisitor;
import lombok.EqualsAndHashCode;
import org.omg.PortableServer.ServantActivator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DiscriminatorValue("VMSPOS")
@EqualsAndHashCode(callSuper = true)
public class VmsPositionFilter extends Filter implements Serializable {

    @Column(name = "MIN_SPEED")
    private String minimumSpeed;

    @Column(name = "MAX_SPEED")
    private String maximumSpeed;

    public String getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(String minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public String getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(String maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVmsPositionFilter(this);
    }
}
