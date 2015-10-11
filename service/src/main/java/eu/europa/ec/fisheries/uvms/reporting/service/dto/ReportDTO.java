package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDTODeserializer;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDTOSerializer;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ReportDTODeserializer.class)
@JsonSerialize(using = ReportDTOSerializer.class)
public class ReportDTO implements Serializable {

    public final static String DESC = "desc";
    public final static String CREATED_BY = "createdBy";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String OUT_COMPONENTS = "outComponents";
    public final static String CREATED_ON = "createdOn";
    public final static String SCOPE_ID = "scopeId";
    public final static String VISIBILITY = "visibility";
    public final static String FILTER_EXPRESSION = "filterExpression";
    public final static String SHAREABLE = "shareable";
    public final static String DELETABLE = "deletable";
    public final static String EDITABLE = "editable";

    private Long id;
    private String name;
    private String description;
    private String outComponents;
    private String scopeName;
    private String createdBy;
    private boolean shareable;
    private boolean editable;
    private boolean deletable;
    private AuditDTO audit;
    private VisibilityEnum visibility;
    private boolean isDeleted;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date deletedOn;

    private String deletedBy;

    private Set<FilterDTO> filters = new HashSet<>(0);

    private Set<ExecutionLogDTO> executionLogs;

    ReportDTO(){}

    @Builder(builderMethodName = "ReportDTOBuilder")
    public ReportDTO(Long id,
                     String name,
                     String description,
                     String outComponents,
                     String scopeName,
                     String createdBy,
                     VisibilityEnum visibility,
                     boolean isDeleted,
                     Date deletedOn,
                     String deletedBy,
                     Set<FilterDTO> filters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.outComponents = outComponents;
        this.scopeName = scopeName;
        this.createdBy = createdBy;
        this.visibility = visibility;
        this.isDeleted = isDeleted;
        this.deletedOn = deletedOn;
        this.deletedBy = deletedBy;
        this.filters = filters;
    }

    public Set<ExecutionLogDTO> filterLogsByUser(final String user){// FIXME
        Set<ExecutionLogDTO> filter = null;
        if (getExecutionLogs() != null){
            Predicate<ExecutionLogDTO> isUserPredicate = new Predicate<ExecutionLogDTO>() {
                public boolean apply(ExecutionLogDTO p) {
                    return p != null && user.equals(p.getExecutedBy());
                }
            };
            filter = new HashSet<>(Collections2.filter(getExecutionLogs(), isUserPredicate));
        }
        return filter;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutComponents() {
        return this.outComponents;
    }

    public void setOutComponents(String outComponents) {
        this.outComponents = outComponents;
    }

    public String getScopeName() {
        return this.scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public AuditDTO getAudit() {
        return audit;
    }

    public void setAudit(AuditDTO audit) {
        this.audit = audit;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDeletedOn() {
        return this.deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Set<ExecutionLogDTO> getExecutionLogs() {
        return this.executionLogs;
    }

    @JsonIgnore
    public void setExecutionLogs(Set<ExecutionLogDTO> reportExecutionLogs) {
        this.executionLogs = reportExecutionLogs;
    }

    public VisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityEnum visibility) {
        this.visibility = visibility;
    }

    public Set<FilterDTO> getFilters() {
        return filters;
    }

    public void setFilters(Set<FilterDTO> filters) {
        this.filters = filters;
    }

    public boolean isShareable() {
        return shareable;
    }

    public void setShareable(boolean shareable) {
        this.shareable = shareable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

}
