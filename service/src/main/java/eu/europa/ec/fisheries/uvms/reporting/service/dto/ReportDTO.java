package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDTODeserializer;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDTOSerializer;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonDeserialize(using = ReportDTODeserializer.class)
@JsonSerialize(using = ReportDTOSerializer.class)
@EqualsAndHashCode(of = {"description" ,"withMap" ,"visibility", "name" ,
        "shareable", "deletable", "editable", "filters", "isDeleted"})
@Data
public class ReportDTO implements Serializable {

    public final static String DESC = "desc";
    public final static String CREATED_BY = "createdBy";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String WITH_MAP = "withMap";
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
    private Boolean withMap;
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

    private List<FilterDTO> filters = new ArrayList<>();

    private Set<ExecutionLogDTO> executionLogs;

    public ReportDTO(){}

    @Builder(builderMethodName = "ReportDTOBuilder")
    public ReportDTO(Long id,
                     String name,
                     String description,
                     Boolean withMap,
                     String scopeName,
                     String createdBy,
                     VisibilityEnum visibility,
                     boolean isDeleted,
                     Date createdOn,
                     Date deletedOn,
                     String deletedBy,
                     List<FilterDTO> filters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.withMap = withMap;
        this.scopeName = scopeName;
        this.createdBy = createdBy;
        this.visibility = visibility;
        this.isDeleted = isDeleted;
        this.deletedOn = deletedOn;
        this.deletedBy = deletedBy;
        this.filters = filters;
        if(audit == null){
            audit = new AuditDTO();
        }
        audit.setCreatedOn(createdOn);
    }

    public Set<ExecutionLogDTO> filterLogsByUser(final String user){// FIXME try hibernate filter
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
}
