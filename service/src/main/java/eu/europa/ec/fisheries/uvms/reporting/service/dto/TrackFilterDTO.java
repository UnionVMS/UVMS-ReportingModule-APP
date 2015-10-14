package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.TrackFilterMapper;
import lombok.Builder;
import org.apache.commons.lang3.NotImplementedException;

public class TrackFilterDTO extends FilterDTO {

    public static final String TRK_MIN_TIME = "trkMinTime";
    public static final String TRK_MAX_TIME = "trkMaxTime";
    public static final String TRK_MIN_DURATION = "trkMinDuration";
    public static final String TRK_MAX_DURATION = "trkMaxDuration";
    public static final String TRACKS = "tracks";

    @JsonProperty(TRK_MAX_TIME)
    private Float maxTime;

    @JsonProperty(TRK_MIN_TIME)
    private Float minTime;

    @JsonProperty(TRK_MIN_DURATION)
    private Float minDuration;

    @JsonProperty(TRK_MAX_DURATION)
    private Float maxDuration;

    @Builder(builderMethodName = "TrackFilterDTOBuild")
    public TrackFilterDTO(Long id,
                          Long reportId,
                          Float maxTime,
                          Float minTime,
                          Float minDuration,
                          Float maxDuration) {
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        setId(id);
        setReportId(reportId);
        setType(FilterType.vmstrack);
    }

    @Override
    public void validate() {
        throw new NotImplementedException("TODO"); // TODO
    }

    @Override
    public Filter convertToFilter() {
        return TrackFilterMapper.INSTANCE.trackFilterDTOToTrackFilter(this);
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
