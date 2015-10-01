package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("VMSPOS")
@EqualsAndHashCode(callSuper = true)
public class VmsPositionFilter extends Filter  {

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

    @Override
    public FilterDTO convertToDTO() {
        return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterToVmsPositionFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsPositionFilter incoming = (VmsPositionFilter) filter;
        this.setMaximumSpeed(incoming.getMaximumSpeed());
        this.setMinimumSpeed(incoming.getMinimumSpeed());
    }
}
