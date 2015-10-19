package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, of = {"minimumSpeed", "maximumSpeed"})
public class VmsSegmentFilterDTO extends FilterDTO {

    public static final String SEG_MIN_SPEED = "segMinSpeed";
    public static final String SEG_MAX_SPEED = "segMaxSpeed";
    public static final String SEG_MIN_TIME = "segMinTime";
    public static final String SEG_MAX_TIME = "segMaxTime";
    public static final String VMS_SEGMENT = "vmssegment";

    @JsonProperty(SEG_MIN_SPEED)
    private Float minimumSpeed;

    @JsonProperty(SEG_MAX_SPEED)
    private Float maximumSpeed;

    @JsonProperty(SEG_MAX_TIME)
    private Float maxTime;

    @JsonProperty(SEG_MIN_TIME)
    private Float minTime;

    @Builder(builderMethodName = "VmsSegmentFilterDTOBuilder")
    public VmsSegmentFilterDTO(Long reportId,
                               Long id,
                               Float maxTime,
                               Float minTime,
                               Float minimumSpeed,
                               Float maximumSpeed) {
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.minTime = minTime;
        this.maxTime = maxTime;
        setReportId(reportId);
        setId(id);
        setType(FilterType.vmsseg);
        validate();
    }

    public VmsSegmentFilterDTO() {

    }

    @Override
    public Filter convertToFilter() {
        return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterDTOToVmsSegmentFilter(this);
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
}
