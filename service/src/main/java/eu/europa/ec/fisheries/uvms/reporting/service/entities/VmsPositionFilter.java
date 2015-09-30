package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterVisitor;
import lombok.EqualsAndHashCode;
import org.omg.PortableServer.ServantActivator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("VMSPOS")
@EqualsAndHashCode(callSuper = true)
public class VmsPositionFilter extends Filter implements Serializable {

    @Column(name = "MIN_SPEED")
    private String minimumSpeed;

    @Column(name = "MAX_SPEED")
    private String maximumSpeed;

    public VmsPositionFilter() {
        super(FilterType.VMSPOS);
    }

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

    public List<ListCriteria> movementListCriteria() {
        List<ListCriteria> listCriterias = new ArrayList<>();
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.SPEED_MAX);
        listCriteria.setValue(maximumSpeed);
        listCriterias.add(listCriteria);
        listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.SPEED_MIN);
        listCriteria.setValue(minimumSpeed);
        listCriterias.add(listCriteria);
        return listCriterias;
    }
}
