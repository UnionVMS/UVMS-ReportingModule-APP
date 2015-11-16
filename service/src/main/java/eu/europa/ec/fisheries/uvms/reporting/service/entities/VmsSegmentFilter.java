package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.collections4.ListUtils;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
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
    private Float minimumSpeed;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed;

    @Column(name = "MIN_DURATION")
    private Float minDuration;

    @Column(name = "MAX_DURATION")
    private Float maxDuration;

    @Column(name = "SEG_CATEGORY")
    private SegmentCategoryType category;

    VmsSegmentFilter(){
        super(FilterType.vmsseg);
    }

    @Builder(builderMethodName = "VmsSegmentFilterBuilder")
    public VmsSegmentFilter(Long id,
                            Float maxDuration, //FIXME replace with DurationRange
                            Float minDuration,
                            Float maximumSpeed,
                            Float minimumSpeed,
                            SegmentCategoryType category) {
        super(FilterType.vmsseg);
        setId(id);
        this.maxDuration = maxDuration;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
        this.minDuration = minDuration;
        this.category = category;
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
        List<ListCriteria> criteria = new ArrayList<>();
        if(getCategory() != null) {
            ListCriteria segmentCategory = new ListCriteria();
            segmentCategory.setKey(SearchKey.CATEGORY);
            segmentCategory.setValue(getCategory().value());
            criteria.add(segmentCategory);
        }
        return criteria;
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {
        return ListUtils.union(getSegmentSpeedCriteria(), getSegmentDurationCriteria());
    }
    
    private List<RangeCriteria> getSegmentSpeedCriteria() {
        if (getMinimumSpeed() == null && getMaximumSpeed() == null) {
            return Collections.emptyList();
        }
        RangeCriteria segmentSpeed = new RangeCriteria();
        segmentSpeed.setKey(RangeKeyType.SEGMENT_SPEED);
        segmentSpeed.setFrom(Float.toString(getMinimumSpeed() != null ? getMinimumSpeed() : 0F));
        segmentSpeed.setTo(Float.toString(getMaximumSpeed()!= null ? getMaximumSpeed() : 1000F));
        return Arrays.asList(segmentSpeed);
    }
    
    private List<RangeCriteria> getSegmentDurationCriteria() {
        if (getMinDuration() == null && getMaxDuration() == null) {
            return Collections.emptyList();
        }
        RangeCriteria segmentDuration = new RangeCriteria();
        segmentDuration.setKey(RangeKeyType.SEGMENT_DURATION);
        segmentDuration.setFrom(Float.toString(getMinDuration() != null ? getMinDuration() : 0F));
        segmentDuration.setTo(Float.toString(getMaxDuration() != null ? getMaxDuration() : 10000F));
        return Arrays.asList(segmentDuration);
    }

    public SegmentCategoryType getCategory() {
        return category;
    }

    public void setCategory(SegmentCategoryType category) {
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
