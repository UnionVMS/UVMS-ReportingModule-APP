package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;

import static java.util.Arrays.asList;
import static org.apache.commons.collections4.ListUtils.union;

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

    VmsSegmentFilter() {
        super(FilterType.vmsseg);
    }

    @Builder
    public VmsSegmentFilter(Long id, DurationRange durationRange, Float maximumSpeed, Float minimumSpeed,
                            SegmentCategoryType category) {
        super(FilterType.vmsseg);
        setId(id);
        this.durationRange = durationRange;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
        this.category = category;
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVmsSegmentFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsSegmentFilterMapper.INSTANCE.merge((VmsSegmentFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return getType();
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        List<ListCriteria> criteria = new ArrayList<>();
        if (category != null) {
            ListCriteria segmentCategory = new ListCriteria();
            segmentCategory.setKey(SearchKey.CATEGORY);
            segmentCategory.setValue(getCategory().value());
            criteria.add(segmentCategory);
        }
        return criteria;
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria(DateTime now) {
        List<RangeCriteria> durationCriteria = new ArrayList<>();
        List<RangeCriteria> speedCriteria = new ArrayList<>();
        if (minimumSpeed != null && maximumSpeed != null) {
            speedCriteria = asList(VmsSegmentFilterMapper.INSTANCE.speedRangeToRangeCriteria(this));
        }
        if (durationRange != null) {
            durationCriteria = asList(VmsSegmentFilterMapper.INSTANCE.durationRangeToRangeCriteria(this));
        }
        return union(speedCriteria, durationCriteria);
    }

    public SegmentCategoryType getCategory() {
        return category;
    }

    public void setCategory(SegmentCategoryType category) {
        this.category = category;
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
