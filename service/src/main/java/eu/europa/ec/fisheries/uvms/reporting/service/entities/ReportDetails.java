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

    @NotNull
    @Column(name = NAME, nullable = false, length = 255)
    private String name; //NOSONAR

    @Column(name = DESCRIPTION, nullable = true, length = 255)
    private String description; //NOSONAR

    @Column(name = WITH_MAP, nullable = false, length = 1)
    @Convert(converter = CharBooleanConverter.class)
    private Boolean withMap; //NOSONAR

    @Column(name = SCOPE_NAME)
    @NotNull
    private String scopeName; //NOSONAR

    @Column(name = CREATED_BY, nullable = false, length = 255)
    @NotNull
    private String createdBy; //NOSONAR

    @Builder(builderMethodName = "ReportDetailsBuilder")
    public ReportDetails(String description, String name, Boolean withMap, String scopeName, String createdBy){
        setName(name);
        setWithMap(withMap);
        setScopeName(scopeName);
        setCreatedBy(createdBy);
        setDescription(description);
    }

    ReportDetails() {

    }
}
