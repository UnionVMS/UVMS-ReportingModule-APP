package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "report", schema = "reporting")
@NamedQueries({
        @NamedQuery(name = Report.LIST_BY_USERNAME_AND_SCOPE, query =
                "SELECT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                        "WHERE ((r.scopeId = :scopeId AND (r.createdBy = :username OR r.visibility = 'SCOPE') ) OR r.visibility = 'PUBLIC') " +
                        "AND r.isDeleted <> 'Y' " +
                        "GROUP BY r.id, l.id"),
        @NamedQuery(name = Report.FIND_BY_ID ,query = "SELECT r FROM Report r WHERE r.id = :reportID AND r.isDeleted <> 'Y'")
})
@Where(clause = "is_deleted <> 'Y'")
@EqualsAndHashCode(exclude = {"executionLogs", "filters", "audit"})
@ToString
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

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "out_components", nullable = false)
    private String outComponents;

    @Column(name = "scope_id", nullable = false)
    private long scopeId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    @Builder
    public Report(String name, String description, String outComponents, long scopeId,
                  String createdBy, Set<Filter> filters,
                  Set<ExecutionLog> executionLogs) {

        this.name = name;
        this.description = description;
        this.outComponents = outComponents;
        this.scopeId = scopeId;
        this.createdBy = createdBy;
        this.visibility = VisibilityEnum.PRIVATE;
        this.filters = filters;
        this.executionLogs = executionLogs;
        this.isDeleted = false;
    }

    Report(){

    }

    public ExecutionLog getExecutionLogByUser(final String username) throws ServiceException {

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
            throw new ServiceException("Error: more then one execution log for the same user");
        }

        if (filter != null && filter.size() == 1){
            result = filter.iterator().next();
        }

        return result;
    }

    @PrePersist
    private void onCreate(){
        audit = new Audit(new Date());
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

    public Set<Filter> getFilters() {
        return this.filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }

    public String getOutComponents() {
        return this.outComponents;
    }

    public void setOutComponents(String outComponents) {
        this.outComponents = outComponents;
    }

    public long getScopeId() {
        return this.scopeId;
    }

    public void setScopeId(long scopeId) {
        this.scopeId = scopeId;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean isIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
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

    public Set<ExecutionLog> getExecutionLogs() {
        return this.executionLogs;
    }

    public void setExecutionLogs(
            Set<ExecutionLog> executionLogs) {
        this.executionLogs = executionLogs;
    }

    public VisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityEnum visibility) {
        this.visibility = visibility;
    }

    public Audit getAudit() {
        return audit;
    }

}