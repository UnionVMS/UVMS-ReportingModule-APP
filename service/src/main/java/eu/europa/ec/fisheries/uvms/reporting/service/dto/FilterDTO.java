package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;

import static com.fasterxml.jackson.annotation.JsonSubTypes.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

@JsonTypeInfo(use = Id.NAME,
        include = As.WRAPPER_OBJECT,
        property = "type")
@JsonSubTypes({
        @Type(value = VmsPositionFilterDTO.class, name = "vmsposition"),
        @Type(value = VesselFilterDTO.class, name = "vessel"),
        @Type(value = VesselGroupFilterDTO.class, name = "vgroup"),
        @Type(value = CommonFilterDTO.class, name = "common"),
        @Type(value = TrackFilterDTO.class, name = "track")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class FilterDTO {

    public static final String ID = "id";
    public static final String TYPE = "type";

    @JsonIgnore
    private Long reportId;

    private Long id;

    private FilterType type;

    public abstract Filter convertToFilter();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
