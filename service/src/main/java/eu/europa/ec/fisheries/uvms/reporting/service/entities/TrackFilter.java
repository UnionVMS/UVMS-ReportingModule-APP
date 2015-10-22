package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.TrackFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("VMSTRACK")
@EqualsAndHashCode(callSuper = true)
public class TrackFilter extends Filter {

    @Column(name = "MAX_TIME")
    private Float maxTime;

    @Column(name = "MIN_TIME")
    private Float minTime;

    @Column(name = "MIN_DURATION")
    private Float minDuration;

    @Column(name = "MAX_DURATION")
    private Float maxDuration;

    TrackFilter() {
        super(FilterType.vmstrack);
    }

    @Builder(builderMethodName = "TrackFilterBuilder")
    public TrackFilter(Long id,
                       Float maxTime,
                       Float maxDuration,
                       Float minDuration,
                       Float minTime) {
        super(FilterType.vmstrack);
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.minTime = minTime;
        this.maxTime = maxTime;
        setId(id);
    }

    @Override
    public FilterDTO convertToDTO() {
        return TrackFilterMapper.INSTANCE.trackFilterToTrackFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        TrackFilter incoming = (TrackFilter) filter;
        setMaxDuration(incoming.getMaxDuration());
        setMaxTime(incoming.getMaxTime());
        setMinDuration(incoming.getMinDuration());
        setMinTime(incoming.getMinTime());
    }

    @Override
    public List<ListCriteria> movementCriteria() {
        ListCriteria listCriteria = new ListCriteria();

        return Lists.newArrayList(listCriteria);
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

}
