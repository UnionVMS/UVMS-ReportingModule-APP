package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import lombok.Builder;
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
    private Float minimumSpeed;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed;

    @Column(name = "MOV_TYPE")
    private MovementTypeType movementType;

    @Column(name = "MOV_ACTIVITY")
    private MovementActivityTypeType movementActivity;

    VmsPositionFilter(){
        super(FilterType.vmspos);
    }

    @Builder(builderMethodName = "VmsPositionFilterBuilder")
    public VmsPositionFilter(Long id,
                             MovementActivityTypeType movementActivity,
                             MovementTypeType movementType,
                             Float maximumSpeed,
                             Float minimumSpeed) {
        super(FilterType.vmspos);
        setId(id);
        this.movementActivity = movementActivity;
        this.movementType = movementType;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
    }

    public List<ListCriteria> movementListCriteria() {
        List<ListCriteria> listCriterias = new ArrayList<>();
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.SPEED_MAX);
        listCriteria.setValue(String.valueOf(maximumSpeed));
        listCriterias.add(listCriteria);
        listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.SPEED_MIN);
        listCriteria.setValue(String.valueOf(minimumSpeed));
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
        setMaximumSpeed(incoming.getMaximumSpeed());
        setMinimumSpeed(incoming.getMinimumSpeed());
        setMovementActivity(incoming.getMovementActivity());
        setMovementType(incoming.getMovementType());
    }

    public Float getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(Float minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public MovementTypeType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementTypeType movementType) {
        this.movementType = movementType;
    }

    public MovementActivityTypeType getMovementActivity() {
        return movementActivity;
    }

    public void setMovementActivity(MovementActivityTypeType movementActivity) {
        this.movementActivity = movementActivity;
    }

    public Float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(Float maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

}
