package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import lombok.EqualsAndHashCode;

import javax.validation.Validation;
import javax.validation.Validator;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode( of = {"id", "type"})
public abstract class FilterDTO {

    public static final String ID = "id";
    public static final String TYPE = "type";

    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @JsonIgnore
    private Long reportId;

    private Long id;

    private FilterType type;

    public abstract Filter convertToFilter();

    public abstract void validate();

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
