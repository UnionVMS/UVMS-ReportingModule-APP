package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Entity
@Table(name = "report", schema = "reporting")
@NamedQueries({
        @NamedQuery(name = Report.LIST_BY_USERNAME_AND_SCOPE, query =
                "SELECT DISTINCT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                    "WHERE ((r.scopeName = :scopeName AND (r.createdBy = :username OR r.visibility = 'SCOPE')) OR r.visibility = 'PUBLIC') " +
                    "AND r.isDeleted <> 'Y' " +
                    "ORDER BY r.id"),
        @NamedQuery(name = Report.FIND_BY_ID, query =
                "SELECT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                    "WHERE r.id = :reportID AND r.isDeleted <> 'Y' AND (r.createdBy = :username " +
                    "OR (r.scopeName = :scopeName AND r.visibility = 'SCOPE') OR r.visibility = 'PUBLIC')")
})
@Where(clause = "is_deleted <> 'Y'")
@EqualsAndHashCode(exclude = {"executionLogs", "filters", "audit"})
@ToString
@Data
@FilterDef(name=Report.EXECUTED_BY_USER, parameters=@ParamDef( name="username", type="string" ) )
public class Report implements Serializable {

    public static final String EXECUTED_BY_USER = "executedByUser";
    public static final String LIST_BY_USERNAME_AND_SCOPE = "Report.listByUsernameAndScope";
    public static final String FIND_BY_ID = "Report.findReportByReportId";
    private static final long serialVersionUID = 7784224707011748170L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "report", cascade = ALL)
    @org.hibernate.annotations.Filter(name=EXECUTED_BY_USER, condition="executed_by = :username")
    private Set<ExecutionLog> executionLogs = new HashSet<>();

    @OneToMany(mappedBy = "report", cascade = ALL)
    private Set<Filter> filters = new HashSet<>();

    @NotNull
    private String name;

    private String description;

    @Column(name = "with_map", nullable = false, length = 1)
    @Convert(converter = CharBooleanConverter.class)
    private Boolean withMap;

    @Column(name = "scope_name")
    @NotNull
    private String scopeName;

    @Column(name = "created_by")
    @NotNull
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @NotNull
    private VisibilityEnum visibility;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_deleted", nullable = true, length = 1)
    private Boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_on")
    private Date deletedOn;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Embedded
    private Audit audit;

    @Builder(builderMethodName = "ReportBuilder")
    public Report(Long id, String name, String description, Boolean withMap, String scopeName,
                  String createdBy, Set<Filter> filters,
                  Set<ExecutionLog> executionLogs, Audit audit) {

        this.name = name;
        this.id = id;
        this.description = description;
        this.withMap = withMap;
        this.scopeName = scopeName;
        this.createdBy = createdBy;
        this.visibility = VisibilityEnum.PRIVATE;
        this.filters = filters;
        this.executionLogs = executionLogs;
        this.isDeleted = false;
        this.audit = audit;
    }

    Report() {

    }

    public void updateExecutionLog(final String username) throws ReportingServiceException {
        ExecutionLog executionLog;
        if (isEmpty(executionLogs)) {
            executionLog = ExecutionLog.builder().report(this).executedBy(username).build();
            executionLogs.add(executionLog);
        } else {
            executionLog = executionLogs.iterator().next();
            executionLog.setExecutedOn(new Date());
            executionLog.setExecutedBy(username);
        }
    }

    public void merge(Report incoming) {
        setId(incoming.getId());
        setName(incoming.getName());
        setDescription(incoming.getDescription());
        setWithMap(incoming.getWithMap());
        setIsDeleted(incoming.getIsDeleted());
        setDeletedOn(incoming.getDeletedOn());
        setDeletedBy(incoming.getDeletedBy());
        setVisibility(incoming.getVisibility());
    }

    @PrePersist
    private void onCreate() {
        audit = new Audit(DateUtils.nowUTC().toDate());
    }

}