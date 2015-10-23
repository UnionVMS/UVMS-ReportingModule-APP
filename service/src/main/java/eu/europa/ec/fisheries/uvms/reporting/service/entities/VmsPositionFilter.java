package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("VMSPOS")
@EqualsAndHashCode(callSuper = true)
public class VmsPositionFilter extends Filter {

	private static final long serialVersionUID = -8391061757398510151L;

	@Column(name = "MIN_SPEED")
    private Float minimumSpeed = 0.0F;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed = 1000F;

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
    
    @Override
    public List<ListCriteria> movementCriteria() {    	
    	List<ListCriteria> criteria = new ArrayList<ListCriteria>();    	
    	if(getMovementType() != null) {
    		ListCriteria movementType = new ListCriteria();
    		movementType.setKey(SearchKey.MOVEMENT_TYPE);
        	movementType.setValue(getMovementType().name());
        	criteria.add(movementType);
    	}    	
    	if(getMovementActivity() != null) {
    		ListCriteria momementActivity = new ListCriteria();
        	momementActivity.setKey(SearchKey.ACTIVITY_TYPE);
        	momementActivity.setValue(getMovementActivity().name());
        	criteria.add(momementActivity);
    	}
    	return criteria;
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {
    	RangeCriteria movementSpeed = new RangeCriteria();
    	movementSpeed.setKey(RangeKeyType.MOVEMENT_SPEED);
    	movementSpeed.setFrom(Float.toString(getMinimumSpeed()));
    	movementSpeed.setTo(Float.toString(getMaximumSpeed()));
    	return Arrays.asList(movementSpeed);
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public Float getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(Float minimumSpeed) {
    	if (minimumSpeed != null) {
    		this.minimumSpeed = minimumSpeed;
    	}        
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
    	if (maximumSpeed != null) {
    		this.maximumSpeed = maximumSpeed;
    	}        
    }

}
