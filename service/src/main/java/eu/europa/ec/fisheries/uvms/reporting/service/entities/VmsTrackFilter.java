package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsTrackFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.*;

@Entity
@DiscriminatorValue("VMSTRACK")
@EqualsAndHashCode(callSuper = true)
@ToString
public class VmsTrackFilter extends Filter {

    private static final float MIN_DEFAULT = 0F;
    private static final float DEFAULT_MAX_AVG_SPEED = Float.MAX_VALUE;
    private static final float DEFAULT_MAX_DISTANCE = Float.MAX_VALUE;
    private static final float DEFAULT_MAX_TIME_AT_SEA = Float.MAX_VALUE;
    private static final float DEFAULT_MAX_FULL_DURATION = Float.MAX_VALUE;

    private @Embedded TimeRange timeRange;
    private @Embedded DurationRange durationRange;
    private @Column(name = "MIN_AVG_SPEED") Float minAvgSpeed;
    private @Column(name = "MAX_AVG_SPEED") Float maxAvgSpeed;

    public VmsTrackFilter() {
        super(vmstrack);
    }

    @Builder
    public VmsTrackFilter(Long id, Long reportId, TimeRange timeRange, DurationRange durationRange, Float minAvgSpeed,
                          Float maxAvgSpeed) {
        super(vmstrack, id, reportId);
        this.timeRange = timeRange;
        this.durationRange = durationRange;
        this.minAvgSpeed = minAvgSpeed;
        this.maxAvgSpeed = maxAvgSpeed;
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVmsTrackFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsTrackFilterMapper.INSTANCE.merge((VmsTrackFilter) filter, this);
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria(DateTime now) {
        List<RangeCriteria> rangeCriteria = new ArrayList<>();
        if (minAvgSpeed != null || maxAvgSpeed != null) {
            rangeCriteria.add(VmsTrackFilterMapper.INSTANCE.speedRangeToRangeCriteria(this));
        }
        if (durationRange != null) {
            rangeCriteria.add(VmsTrackFilterMapper.INSTANCE.durationRangeToRangeCriteria(this));
        }
        if (timeRange != null) {
            rangeCriteria.add(VmsTrackFilterMapper.INSTANCE.timeRangeToRangeCriteria(this) );
        }
        return rangeCriteria;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public DurationRange getDurationRange() {
        return durationRange;
    }

    public void setDurationRange(DurationRange durationRange) {
        this.durationRange = durationRange;
    }

    @Override
    public Object getUniqKey() {
        return getType();
    }

    public Float getMinAvgSpeed() {
        return minAvgSpeed;
    }

    public void setMinAvgSpeed(Float minAvgSpeed) {
        this.minAvgSpeed = minAvgSpeed;
    }

    public Float getMaxAvgSpeed() {
        return maxAvgSpeed;
    }

    public void setMaxAvgSpeed(Float maxAvgSpeed) {
        this.maxAvgSpeed = maxAvgSpeed;
    }

}
