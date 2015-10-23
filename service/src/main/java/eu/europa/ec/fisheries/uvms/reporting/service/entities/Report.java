package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "report", schema = "reporting")
@NamedQueries({
        @NamedQuery(name = Report.LIST_BY_USERNAME_AND_SCOPE, query =
                "SELECT DISTINCT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                        "WHERE ((r.scopeName = :scopeName AND (r.createdBy = :username OR r.visibility = 'SCOPE') ) OR r.visibility = 'PUBLIC') " +
                        "AND r.isDeleted <> 'Y' " +
                        "GROUP BY r.id, l.id"),
        @NamedQuery(name = Report.FIND_BY_ID , query = "SELECT r FROM Report r " +
                        "WHERE r.id = :reportID AND r.isDeleted <> 'Y' AND (r.createdBy = :username " +
                        "OR (r.scopeName = :scopeName AND r.visibility = 'SCOPE') OR r.visibility = 'PUBLIC')")
})
@Where(clause = "is_deleted <> 'Y'")
@EqualsAndHashCode(exclude = {"executionLogs", "filters", "audit"})
@ToString
@Data
public class Report implements Serializable {

    private static final long serialVersionUID = 7784224707011748170L;
    public static final String LIST_BY_USERNAME_AND_SCOPE = "Report.listByUsernameAndScope";
    public static final String FIND_BY_ID = "Report.findReportByReportId";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "report", cascade = ALL)
    private Set<ExecutionLog> executionLogs = new HashSet<>();

    @OneToMany(mappedBy = "report", cascade = ALL)
    private Set<Filter> filters = new HashSet<>();

    @NotNull
    private String name;

    private String description;

    @Column(name = "with_map", nullable = false, length=1)
    @Convert(converter=CharBooleanConverter.class)
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

    @Convert(converter=CharBooleanConverter.class)
    @Column(name = "is_deleted", nullable = true, length=1)
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

    Report(){

    }

    public Report updateExecutionLog(final String username) throws ReportingServiceException {
        ExecutionLog executionLogByUser = getExecutionLogByUser(username);

        if(executionLogByUser != null){
            executionLogByUser.setExecutedOn(new Date());
        }
        else {
            ExecutionLog executionLog =
                    ExecutionLog.builder().report(this).executedBy(username).build();
            if (executionLogs == null){
                executionLogs = new HashSet<>();
            }
            executionLogs.add(executionLog);
        }
        return this;
    }

    public ExecutionLog getExecutionLogByUser(final String username) throws ReportingServiceException {

        ExecutionLog result = null;
        Collection<ExecutionLog> filter = null;

        if (getExecutionLogs() != null){
            Predicate<ExecutionLog> isUserPredicate = new Predicate<ExecutionLog>() {
                public boolean apply(ExecutionLog p) {
                    return p != null && username.equals(p.getExecutedBy());
                }
            };
            filter = Collections2.filter(executionLogs, isUserPredicate);
        }

        if (filter != null && filter.size() > 1){
            throw new ReportingServiceException("Error: more then one execution log for the same user");
        }

        if (filter != null && filter.size() == 1){
            result = filter.iterator().next();
        }

        return result;
    }

    public void merge(Report incoming){
        setId(incoming.getId());
        setName(incoming.getName());
        setDescription(incoming.getDescription());
        setWithMap(incoming.getWithMap());
        setIsDeleted(incoming.getIsDeleted());
        setDeletedOn(incoming.getDeletedOn());
        setDeletedBy( incoming.getDeletedBy() );
        setVisibility( incoming.getVisibility() );
    }

    @PrePersist
    private void onCreate() {
        audit = new Audit(DateUtils.nowUTC().toDate());
    }

}