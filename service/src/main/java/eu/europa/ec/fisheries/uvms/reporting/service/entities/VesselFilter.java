package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterVisitor;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("VESSEL")
@EqualsAndHashCode(callSuper = true)
public class VesselFilter extends Filter implements Serializable {

    private String guid;

    private String name;

    public VesselFilter() {
        super(FilterType.VESSEL);
    }

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

    public VesselListCriteriaPair vesselListCriteriaPair(){
        VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(guid);
        return criteriaPair;
    }
}
