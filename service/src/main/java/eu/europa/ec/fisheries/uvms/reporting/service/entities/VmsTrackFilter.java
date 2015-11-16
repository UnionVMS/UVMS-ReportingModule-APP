package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterVisitor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.valueOf;

@Entity
@DiscriminatorValue("VMSTRACK")
@EqualsAndHashCode(callSuper = true)
public class VmsTrackFilter extends Filter {

    private static final float MIN_DEFAULT = 0F;
    private static final float DEFAULT_MAX_AVG_SPEED = 9999999F;
    private static final float DEFAULT_MAX_DISTANCE = 9999999F;
    private static final float DEFAULT_MAX_TIME_AT_SEA = 9999999F;
    private static final float DEFAULT_MAX_FULL_DURATION = 9999999F;

    @Embedded
    private TimeRange timeRange;

    @Embedded
    private DurationRange durationRange;

    @Embedded
    private DistanceRange distanceRange;

    @Column(name = "MIN_AVG_SPEED")
    private Float minAvgSpeed;

    @Column(name = "MAX_AVG_SPEED")
    private Float maxAvgSpeed;

    public VmsTrackFilter() {
        super(FilterType.vmstrack);
    }

    @Builder
    public VmsTrackFilter(Long id, Long reportId,
                          TimeRange timeRange,
                          DurationRange durationRange,
                          DistanceRange distanceRange,
                          Float minAvgSpeed,
                          Float maxAvgSpeed) {
        super(FilterType.vmstrack, id, reportId);
        this.timeRange = timeRange;
        this.distanceRange = distanceRange;
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
        VmsTrackFilter incoming = (VmsTrackFilter) filter;
        setDurationRange(incoming.getDurationRange());
        setTimeRange(incoming.getTimeRange());
        setDistanceRange(incoming.getDistanceRange());
        setMaxAvgSpeed(incoming.getMaxAvgSpeed());
        setMinAvgSpeed(incoming.getMinAvgSpeed());
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {
        List<RangeCriteria> rangeCriteria = newArrayList();
        addDurationAtSeaCriteria(rangeCriteria);
        addTotalDurationCriteria(rangeCriteria);
        addSpeedCriteria(rangeCriteria);
        addDistanceCriteria(rangeCriteria);
        return rangeCriteria;
    }

    private void addSpeedCriteria(List<RangeCriteria> rangeCriteria) {
        if (minAvgSpeed != null || maxAvgSpeed != null) {
            RangeCriteria lengthCriteria = new RangeCriteria();
            lengthCriteria.setKey(RangeKeyType.TRACK_SPEED);
            lengthCriteria.setFrom(valueOf(minAvgSpeed != null ? minAvgSpeed : MIN_DEFAULT));
            lengthCriteria.setTo(valueOf(maxAvgSpeed != null ? maxAvgSpeed : DEFAULT_MAX_AVG_SPEED));
            rangeCriteria.add(lengthCriteria);
        }
    }

    private void addDistanceCriteria(List<RangeCriteria> rangeCriteria) {
        Float maxDistance = distanceRange.getMaxDistance();
        Float minDistance = distanceRange.getMinDistance();
        if (minDistance != null || maxDistance != null) {
            RangeCriteria lengthCriteria = new RangeCriteria();
            lengthCriteria.setKey(RangeKeyType.TRACK_LENGTH);
            lengthCriteria.setFrom(valueOf(minDistance != null ? minDistance : MIN_DEFAULT));
            lengthCriteria.setTo(valueOf(maxDistance != null ? maxDistance : DEFAULT_MAX_DISTANCE));
            rangeCriteria.add(lengthCriteria);
        }
    }

    private void addTotalDurationCriteria(List<RangeCriteria> rangeCriteria) {
        Float maxDuration = durationRange.getMaxDuration();
        Float minDuration = durationRange.getMinDuration();
        if (minDuration != null || maxDuration != null) {
            RangeCriteria durationCriteria = new RangeCriteria();
            durationCriteria.setKey(RangeKeyType.TRACK_DURATION);
            durationCriteria.setFrom(valueOf(minDuration != null ? minDuration : MIN_DEFAULT));
            durationCriteria.setTo(valueOf(maxDuration != null ? maxDuration : DEFAULT_MAX_FULL_DURATION));
            rangeCriteria.add(durationCriteria);
        }
    }

    private void addDurationAtSeaCriteria(List<RangeCriteria> rangeCriteria) {
        Float minTime = timeRange.getMinTime();
        Float maxTime = timeRange.getMaxTime();
        if (minTime != null || maxTime != null) {
            RangeCriteria timeCriteria = new RangeCriteria();
            timeCriteria.setKey(RangeKeyType.TRACK_DURATION_AT_SEA);
            timeCriteria.setFrom(valueOf(minTime != null ? minTime : MIN_DEFAULT));
            timeCriteria.setTo(valueOf(maxTime != null ? maxTime : DEFAULT_MAX_TIME_AT_SEA));
            rangeCriteria.add(timeCriteria);
        }
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

    public DistanceRange getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(DistanceRange distanceRange) {
        this.distanceRange = distanceRange;
    }

    @Override
    public Object getUniqKey() {
        return getId();
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
