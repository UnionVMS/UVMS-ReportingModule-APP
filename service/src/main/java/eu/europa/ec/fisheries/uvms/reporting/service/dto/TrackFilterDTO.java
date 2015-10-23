package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.TrackFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.mapstruct.factory.Mappers;

@EqualsAndHashCode(callSuper = true)
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

    public TrackFilterDTO() {
        super(FilterType.vmstrack);
    }

    public TrackFilterDTO(Long id, Long reportId) {
        super(FilterType.vmstrack, id, reportId);
    }

    @Builder(builderMethodName = "TrackFilterDTOBuild")
    public TrackFilterDTO(Long id,
                          Long reportId,
                          Float maxTime,
                          Float minTime,
                          Float minDuration,
                          Float maxDuration) {
        this(id, reportId);
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        validate();
    }

    @Override
    public Filter convertToFilter() {
        TrackFilterMapper INSTANCE = Mappers.getMapper(TrackFilterMapper.class);
        return INSTANCE.trackFilterDTOToTrackFilter(this);
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
