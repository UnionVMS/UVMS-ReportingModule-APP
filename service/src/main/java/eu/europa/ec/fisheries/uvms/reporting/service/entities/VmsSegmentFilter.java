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
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("VMSSEG")
@EqualsAndHashCode(callSuper = true)
public class VmsSegmentFilter extends Filter {

	private static final long serialVersionUID = -8657712390731877147L;

	@Column(name = "MIN_SPEED")
    private Float minimumSpeed = 0.0F;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed = 1000F;

    @Column(name = "MIN_DURATION")
    private Float minDuration = 0F;

    @Column(name = "MAX_DURATION")
    private Float maxDuration = 1000F;

    @Column(name = "SEG_CATEGORY")
    private String category;


    VmsSegmentFilter(){
        super(FilterType.vmsseg);
    }

    @Builder(builderMethodName = "VmsSegmentFilterBuilder")
    public VmsSegmentFilter(Long id,
                            Float maxDuration,
                            Float minDuration,
                            Float maximumSpeed,
                            Float minimumSpeed,
                            String category) {
        super(FilterType.vmsseg);
        setId(id);
        this.maxDuration = maxDuration;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
        this.minDuration = minDuration;
        this.category   = category;
    }
    @Override
    public FilterDTO convertToDTO() {
        return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterToVmsSegmentFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsSegmentFilter incoming = (VmsSegmentFilter) filter;
        setMaximumSpeed(incoming.getMaximumSpeed());
        setMinimumSpeed(incoming.getMinimumSpeed());
        setMinDuration(incoming.getMinDuration());
        setMaxDuration(incoming.getMaxDuration());
        setCategory(incoming.getCategory());
    }
    
    @Override
    public List<ListCriteria> movementCriteria() {
    	List<ListCriteria> criteria = new ArrayList<ListCriteria>();
    	if(getCategory() != null) {
    		ListCriteria segmentCatagory = new ListCriteria();
        	segmentCatagory.setKey(SearchKey.CATEGORY);
        	segmentCatagory.setValue(getCategory());
        	criteria.add(segmentCatagory);
    	}    	
    	return criteria;
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {
    	RangeCriteria segmentSpeed = new RangeCriteria();
    	segmentSpeed.setKey(RangeKeyType.SEGMENT_SPEED);
    	segmentSpeed.setFrom(Float.toString(getMinimumSpeed()));
    	segmentSpeed.setTo(Float.toString(getMaximumSpeed()));
    	
    	RangeCriteria segmentDuration = new RangeCriteria();
    	segmentDuration.setKey(RangeKeyType.SEGMENT_DURATION);
    	segmentDuration.setFrom(Float.toString(getMinDuration()));
    	segmentDuration.setTo(Float.toString(getMaxDuration()));
    	
    	return Arrays.asList(segmentSpeed, segmentDuration);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public Float getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(Float minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public Float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(Float maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
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
