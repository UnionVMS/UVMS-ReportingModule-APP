package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.collect.Lists;
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
import java.util.List;

@Entity
@DiscriminatorValue("VMSTRACK")
@EqualsAndHashCode(callSuper = true)
public class VmsTrackFilter extends Filter {

    @Column(name = "MIN_TIME")
    private Float minTime = 0F;
    ;

    @Column(name = "MAX_TIME")
    private Float maxTime = 5000F;
    ;

    @Column(name = "MIN_DURATION")
    private Float minDuration = 0F;

    @Column(name = "MAX_DURATION")
    private Float maxDuration = 5000F;

    public VmsTrackFilter() {
        super(FilterType.vmstrack);
    }

    @Builder(builderMethodName = "TrackFilterBuilder")
    public VmsTrackFilter(Long id, Long reportId,
                          Float maxTime,
                          Float maxDuration,
                          Float minDuration,
                          Float minTime) {
        super(FilterType.vmstrack, id, reportId);
        setMinDuration(minDuration);
        setMaxDuration(maxDuration);
        setMinTime(minTime);
        setMaxTime(maxTime);
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
        setMinDuration(incoming.getMinDuration());
        setMinTime(incoming.getMinTime());
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria() {
        RangeCriteria timeCriteria = new RangeCriteria();
        timeCriteria.setKey(RangeKeyType.TRACK_DURATION_AT_SEA);
        timeCriteria.setFrom(String.valueOf(minTime));
        timeCriteria.setTo(String.valueOf(maxTime));

        RangeCriteria durationCriteria = new RangeCriteria();
        durationCriteria.setKey(RangeKeyType.TRACK_DURATION);
        durationCriteria.setFrom(String.valueOf(minDuration));
        durationCriteria.setTo(String.valueOf(maxDuration));

        return Lists.newArrayList(timeCriteria, durationCriteria);
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public Float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Float maxTime) {
        if (maxTime != null) {
            this.maxTime = maxTime;
        }
    }

    public Float getMinTime() {
        return minTime;
    }

    public void setMinTime(Float minTime) {
        if (minTime != null) {
            this.minTime = minTime;
        }
    }

    public Float getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Float minDuration) {
        if (minDuration != null) {
            this.minDuration = minDuration;
        }
    }

    public Float getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Float maxDuration) {
        if (maxDuration != null) {
            this.maxDuration = maxDuration;
        }
    }

}
