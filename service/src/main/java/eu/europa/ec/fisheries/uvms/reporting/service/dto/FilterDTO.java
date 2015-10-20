package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import lombok.EqualsAndHashCode;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode( of = {"id"})
public abstract class FilterDTO {

    public static final String ID = "id";
    public static final String TYPE = "type";

    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @JsonIgnore
    private Long reportId;

    private Long id;

    private FilterType type;

    public abstract Filter convertToFilter();

    public void validate() {
        Set<ConstraintViolation<FilterDTO>> violations =
                validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
        }
    }

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
