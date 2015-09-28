package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterVisitor;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("VESSEL")
@EqualsAndHashCode(callSuper = true)
public class VesselFilter extends Filter implements Serializable {

    private String guid;

    private String name;

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVesselFilter(this);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
