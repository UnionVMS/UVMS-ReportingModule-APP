package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.Builder;
import org.apache.commons.lang3.NotImplementedException;

public class VmsSegmentFilterDTO extends FilterDTO {

    public static final String MOV_MIN_SPEED = "movMinSpeed";
    public static final String MOV_MAX_SPEED = "movMaxSpeed";
    public static final String TRK_MIN_TIME = "trkMinTime";
    public static final String TRK_MAX_TIME = "trkMaxTime";

    @JsonProperty(MOV_MIN_SPEED)
    private Float minimumSpeed;

    @JsonProperty(MOV_MAX_SPEED)
    private Float maximumSpeed;

    @JsonProperty(TRK_MAX_TIME)
    private Float maxTime;

    @JsonProperty(TRK_MIN_TIME)
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
    }

    @Override
    public Filter convertToFilter() {
        return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterDTOToVmsSegmentFilter(this);
    }

    @Override
    public void validate() {
        throw new NotImplementedException("TODO"); // TODO
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
