package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;

import static com.fasterxml.jackson.annotation.JsonSubTypes.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

@JsonTypeInfo(use = Id.NAME,
        include = As.WRAPPER_OBJECT,
        property = "type")
@JsonSubTypes({
        @Type(value = VmsPositionFilterDTO.class, name = "vmsposition"),
        @Type(value = PositionFilterDTO.class, name = "position"),
        @Type(value = VesselFilterDTO.class, name = "vessel"),
        @Type(value = VesselGroupFilterDTO.class, name = "vgroup")
})
public abstract class FilterDTO {

    private Long reportId; // FIXME try using ReportDTO
    private Long id;

    public abstract Filter convertToFilter();

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
