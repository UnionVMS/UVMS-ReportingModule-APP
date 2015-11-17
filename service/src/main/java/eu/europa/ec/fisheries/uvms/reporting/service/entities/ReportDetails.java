package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class ReportDetails {

    public static final String CREATED_BY = "created_by";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String WITH_MAP = "with_map";
    public static final String SCOPE_NAME = "scope_name";
    public static final int LENGTH_255 = 255;
    public static final int LENGTH_1 = 1;

    @NotNull
    @Column(name = NAME, nullable = false, length = LENGTH_255)
    private String name;

    @Column(name = DESCRIPTION, nullable = true, length = LENGTH_255)
    private String description;

    @Column(name = WITH_MAP, nullable = false, length = LENGTH_1)
    @Convert(converter = CharBooleanConverter.class)
    private Boolean withMap;

    @Column(name = SCOPE_NAME)
    @NotNull
    private String scopeName;

    @Column(name = CREATED_BY, nullable = false, length = LENGTH_255)
    @NotNull
    private String createdBy;

    @Builder
    public ReportDetails(String description, String name, Boolean withMap, String scopeName, String createdBy){
        this.name = name;
        this.withMap = withMap;
        this.scopeName = scopeName;
        this.createdBy = createdBy;
        this.description = description;
    }

    ReportDetails() {

    }
}
