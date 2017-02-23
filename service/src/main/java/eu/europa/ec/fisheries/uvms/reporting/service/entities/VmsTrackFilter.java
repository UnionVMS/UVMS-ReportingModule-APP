/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.domain.Range;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsTrackFilterMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;
import java.util.concurrent.TimeUnit;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.vmstrack;

@Entity
@DiscriminatorValue("VMSTRACK")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VmsTrackFilter extends Filter {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="min", column=@Column(name="MIN_TIME")),
            @AttributeOverride(name="max", column=@Column(name="MAX_TIME"))
    })
    private Range timeRange;

    @Embedded
    private DurationRange durationRange;

    @Column(name = "MIN_AVG_SPEED")
    private Float minAvgSpeed;

    @Column(name = "MAX_AVG_SPEED")
    private Float maxAvgSpeed;

    public VmsTrackFilter() {
        super(vmstrack);
    }

    @Builder
    public VmsTrackFilter(Long reportId, Range timeRange, DurationRange durationRange, Float minAvgSpeed,
                          Float maxAvgSpeed) {
        super(vmstrack, reportId);
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
    public Object getUniqKey() {
        return getType();
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
            rangeCriteria.add(VmsTrackFilterMapper.INSTANCE.timeRangeToRangeCriteria(this));
        }
        return rangeCriteria;
    }

    public Range getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(Range timeRange) {
        this.timeRange = timeRange;
    }

    public DurationRange getDurationRange() {
        return durationRange;
    }

    public void setDurationRange(DurationRange durationRange) {
        this.durationRange = durationRange;
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

    public String convertedMinTime(){
        Long minTime = timeRange.getMin() != null ? timeRange.getMin().longValue() : 0;
        return String.valueOf(TimeUnit.MILLISECONDS.convert(minTime, TimeUnit.HOURS));
    }

    public String convertedMaxTime(){
        Long maxTime = timeRange.getMax() != null ? timeRange.getMax().longValue() : Long.MAX_VALUE;
        return String.valueOf(TimeUnit.MILLISECONDS.convert(maxTime, TimeUnit.HOURS));
    }

    public String convertedMaxDuration(){
        Long maxDuration = durationRange.getMaxDuration() != null ? durationRange.getMaxDuration().longValue() : Long.MAX_VALUE;
        return String.valueOf(TimeUnit.MILLISECONDS.convert(maxDuration, TimeUnit.HOURS));
    }

    public String convertedMinDuration(){
        Long minDuration = durationRange.getMinDuration() != null ? durationRange.getMinDuration().longValue() : 0L;
        return String.valueOf(TimeUnit.MILLISECONDS.convert(minDuration, TimeUnit.HOURS));
    }

}