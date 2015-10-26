package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.TrackFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.mapstruct.factory.Mappers;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
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

    @Column(name = "MIN_TIME")
    private Float minTime;

    @Column(name = "MAX_TIME")
    private Float maxTime;

    @Column(name = "MIN_DURATION")
    private Float minDuration;

    @Column(name = "MAX_DURATION")
    private Float maxDuration;

    @Column(name = "MIN_DISTANCE")
    private Float minDistance;

    @Column(name = "MAX_DISTANCE")
    private Float maxDistance;

    @Column(name = "MIN_AVG_SPEED")
    private Float minAvgSpeed;

    @Column(name = "MAX_AVG_SPEED")
    private Float maxAvgSpeed;

    public VmsTrackFilter() {
        super(FilterType.vmstrack);
    }

    @Builder(builderMethodName = "TrackFilterBuilder")
    public VmsTrackFilter(Long id, Long reportId,
                          Float maxTime,
                          Float maxDuration,
                          Float minDuration,
                          Float minTime,
                          Float minAvgSpeed,
                          Float maxAvgSpeed,
                          Float minDistance,
                          Float maxDistance) {
        super(FilterType.vmstrack, id, reportId);
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.minAvgSpeed = minAvgSpeed;
        this.maxAvgSpeed = maxAvgSpeed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public FilterDTO convertToDTO() {
        TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);
        return INSTANCE.trackFilterToTrackFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsTrackFilter incoming = (VmsTrackFilter) filter;
        setMaxDuration(incoming.getMaxDuration());
        setMaxTime(incoming.getMaxTime());
        setMaxAvgSpeed(incoming.getMaxAvgSpeed());
        setMaxDistance(incoming.getMaxDistance());

        setMinDuration(incoming.getMinDuration());
        setMinTime(incoming.getMinTime());
        setMinAvgSpeed(incoming.getMinAvgSpeed());
        setMinDistance(incoming.getMinDistance());
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {
        ArrayList<RangeCriteria> rangeCriterias = newArrayList();

        addDurationAtSeaCriteria(rangeCriterias);
        addTotalDurationCriteria(rangeCriterias);
        addSpeedCriteria(rangeCriterias);
        addDistanceCriteria(rangeCriterias);

        return rangeCriterias;
    }

    private void addSpeedCriteria(ArrayList<RangeCriteria> rangeCriterias) {
        if (minAvgSpeed != null || maxAvgSpeed != null) {
            RangeCriteria lengthCriteria = new RangeCriteria();
            lengthCriteria.setKey(RangeKeyType.TRACK_SPEED);
            lengthCriteria.setFrom(valueOf(minAvgSpeed != null ? minAvgSpeed : MIN_DEFAULT));
            lengthCriteria.setTo(valueOf(maxAvgSpeed != null ? maxAvgSpeed : DEFAULT_MAX_AVG_SPEED));
            rangeCriterias.add(lengthCriteria);
        }
    }

    private void addDistanceCriteria(ArrayList<RangeCriteria> rangeCriterias) {
        if (minDistance != null || maxDistance != null) {
            RangeCriteria lengthCriteria = new RangeCriteria();
            lengthCriteria.setKey(RangeKeyType.TRACK_LENGTH);
            lengthCriteria.setFrom(valueOf(minDistance != null ? minDistance : MIN_DEFAULT));
            lengthCriteria.setTo(valueOf(maxDistance != null ? maxDistance : DEFAULT_MAX_DISTANCE));
            rangeCriterias.add(lengthCriteria);
        }
    }

    private void addTotalDurationCriteria(ArrayList<RangeCriteria> rangeCriterias) {
        if (minDuration != null || maxDuration != null) {
            RangeCriteria durationCriteria = new RangeCriteria();
            durationCriteria.setKey(RangeKeyType.TRACK_DURATION);
            durationCriteria.setFrom(valueOf(minDuration != null ? minDuration : MIN_DEFAULT));
            durationCriteria.setTo(valueOf(maxDuration != null ? maxDuration : DEFAULT_MAX_FULL_DURATION));
            rangeCriterias.add(durationCriteria);
        }
    }

    private void addDurationAtSeaCriteria(ArrayList<RangeCriteria> rangeCriterias) {
        if (minTime != null || maxTime != null) {
            RangeCriteria timeCriteria = new RangeCriteria();
            timeCriteria.setKey(RangeKeyType.TRACK_DURATION_AT_SEA);
            timeCriteria.setFrom(valueOf(minTime != null ? minTime : MIN_DEFAULT));
            timeCriteria.setTo(valueOf(maxTime != null ? maxTime : DEFAULT_MAX_TIME_AT_SEA));
            rangeCriterias.add(timeCriteria);
        }
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public Float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Float maxTime) {
        this.maxTime = maxTime;
    }

    public Float getMinTime() {
        return minTime;
    }

    public void setMinTime(Float minTime) {
        this.minTime = minTime;
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

    public Float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(Float minDistance) {
        this.minDistance = minDistance;
    }

    public Float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Float maxDistance) {
        this.maxDistance = maxDistance;
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
