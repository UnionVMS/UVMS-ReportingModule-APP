package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterVisitor;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.ToString;
import org.apache.commons.collections4.ListUtils;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("VMSSEG")
@EqualsAndHashCode(callSuper = true)
@ToString
public class VmsSegmentFilter extends Filter {

    @Column(name = "MIN_SPEED")
    private Float minimumSpeed;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed;

    @Embedded
    private DurationRange durationRange;

    @Column(name = "SEG_CATEGORY")
    private SegmentCategoryType category;

    VmsSegmentFilter(){
        super(FilterType.vmsseg);
    }

    @Builder
    public VmsSegmentFilter(Long id,
                            DurationRange durationRange,
                            Float maximumSpeed,
                            Float minimumSpeed,
                            SegmentCategoryType category) {
        super(FilterType.vmsseg);
        setId(id);
        this.durationRange = durationRange;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
        this.category = category;
    }

    @Override
    public void merge(Filter filter) {
        VmsSegmentFilterMapper.INSTANCE.merge((VmsSegmentFilter) filter, this);
    }
    
    @Override
    public List<ListCriteria> movementListCriteria() {
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
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVmsSegmentFilter(this);
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {

        return ListUtils.union(getSegmentSpeedCriteria(), getSegmentDurationCriteria());

    }
    
    private List<RangeCriteria> getSegmentSpeedCriteria() {

        if (this.minimumSpeed == null && this.maximumSpeed == null) {

            return Collections.emptyList();
        }

        return Arrays.asList(VmsSegmentFilterMapper.INSTANCE.speedRangeToRangeCriteria(this));
    }
    
    private List<RangeCriteria> getSegmentDurationCriteria() {

        if (this.durationRange == null) {

            return Collections.emptyList();

        }

        return Arrays.asList(VmsSegmentFilterMapper.INSTANCE.durationRangeToRangeCriteria(this));


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

    public DurationRange getDurationRange() {

        return durationRange;
    }

    public void setDurationRange(DurationRange durationRange) {

        this.durationRange = durationRange;
    }
}
