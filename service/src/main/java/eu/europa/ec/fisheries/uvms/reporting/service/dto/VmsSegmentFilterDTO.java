package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, of = {"minimumSpeed", "maximumSpeed", "maxDuration", "minDuration", "category"})
public class VmsSegmentFilterDTO extends FilterDTO {

    public static final String SEG_MIN_SPEED = "segMinSpeed";
    public static final String SEG_MAX_SPEED = "segMaxSpeed";
    public static final String SEG_MIN_DURATION = "segMinDuration";
    public static final String SEG_MAX_DURATION = "segMaxDuration";
    public static final String SEG_CATEGORY = "segCategory";
    public static final String VMS_SEGMENT = "vmssegment";

    @JsonProperty(SEG_MIN_SPEED)
    private Float minimumSpeed;

    @JsonProperty(SEG_MAX_SPEED)
    private Float maximumSpeed;

    @JsonProperty(SEG_MAX_DURATION)
    private Float maxDuration;

    @JsonProperty(SEG_MIN_DURATION)
    private Float minDuration;

    @JsonProperty(SEG_CATEGORY)
    private String category;

    @Builder(builderMethodName = "VmsSegmentFilterDTOBuilder")
    public VmsSegmentFilterDTO(Long reportId,
                               Long id,
                               Float maxDuration,
                               Float minDuration,
                               Float minimumSpeed,
                               Float maximumSpeed,
                               String category) {
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.category = category;
        setReportId(reportId);
        setId(id);
        setType(FilterType.vmsseg);
        validate();
    }

    public VmsSegmentFilterDTO() {

    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Float getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Float maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Float getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Float minDuration) {
        this.minDuration = minDuration;
    }
}
