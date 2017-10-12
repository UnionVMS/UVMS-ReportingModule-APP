/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import static javax.persistence.CascadeType.ALL;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.ReportTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "report")
@NamedQueries({
        @NamedQuery(name = Report.LIST_TOP_EXECUTED_BY_DATE, query =
                "SELECT DISTINCT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                        "WHERE ((r.details.scopeName = :scopeName AND (r.details.createdBy = :username OR r.visibility = 'SCOPE')) OR r.visibility = 'PUBLIC') " +
                        "AND r.isDeleted <> :existent " +
                        "AND l.executedBy = :username " +
                        "ORDER BY l.executedOn DESC"),
        @NamedQuery(name = Report.LIST_BY_CREATION_DATE, query =
                "SELECT DISTINCT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                        "WHERE ((1=:isAdmin) " +
                        "OR ((r.details.scopeName = :scopeName AND (r.details.createdBy = :username OR r.visibility = 'SCOPE')) " +
                        "OR r.visibility = 'PUBLIC')) " +
                        "AND r.isDeleted <> :existent AND l IS NULL " +
                        "ORDER BY r.audit.createdOn DESC"),
        @NamedQuery(name = Report.FIND_BY_ID, query =
                "SELECT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                        "WHERE r.id = :reportID " +
                        "AND r.isDeleted <> 'Y' " +
                        "AND ((1=:isAdmin) " +
                        "OR ((r.details.scopeName = :scopeName AND (r.details.createdBy = :username OR r.visibility = 'SCOPE')) " +
                        "OR r.visibility = 'PUBLIC'))"),
        @NamedQuery(name = Report.LIST_BY_USERNAME_AND_SCOPE, query =
                "SELECT DISTINCT r FROM Report r LEFT JOIN FETCH r.executionLogs l " +
                        "WHERE r.isDeleted <> :existent " +
                        "AND ((1=:isAdmin) " +
                        "OR ((r.details.scopeName = :scopeName AND (r.details.createdBy = :username OR r.visibility = 'SCOPE')) " +
                        "OR r.visibility = 'PUBLIC'))" +
                        "ORDER BY r.id")
})
@Where(clause = "is_deleted <> 'Y'")
@EqualsAndHashCode(callSuper = false, exclude = {"executionLogs", "filters", "audit"})
@ToString(callSuper = true)
@Data
@FilterDef(name = Report.EXECUTED_BY_USER, parameters = @ParamDef(name = "username", type = "string"))
public class Report extends BaseEntity {

    public static final String IS_DELETED = "is_deleted";
    public static final String REPORT_TYPE = "report_type";
    public static final String EXECUTED_BY_USER = "executedByUser";
    public static final String LIST_BY_USERNAME_AND_SCOPE = "Report.listByUsernameAndScope";
    public static final String LIST_TOP_EXECUTED_BY_DATE = "Report.listTopExecutedByDate";
    public static final String LIST_BY_CREATION_DATE = "Report.listByCreationDate";
    public static final String FIND_BY_ID = "Report.findReportByReportId";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="report_seq", sequenceName="report_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="report_seq")
	private Long id;

	
    @OneToMany(mappedBy = "report", cascade = ALL)
    @org.hibernate.annotations.Filter(name = EXECUTED_BY_USER, condition = "executed_by = :username")
    private Set<ExecutionLog> executionLogs = new HashSet<>();

    @OneToMany(mappedBy = "report", cascade = ALL)
    private Set<Filter> filters = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private VisibilityEnum visibility = VisibilityEnum.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(name = REPORT_TYPE)
    private ReportTypeEnum reportType = ReportTypeEnum.STANDARD;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = IS_DELETED, length = 1)
    private Boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_on")
    private Date deletedOn;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Embedded
    private ReportDetails details = new ReportDetails();

    @Embedded
    private Audit audit = new Audit();

    @Builder
    public Report(ReportDetails details, String createdBy, Set<Filter> filters,
                  Set<ExecutionLog> executionLogs, Audit audit) {
        this.details = details;
        this.visibility = VisibilityEnum.PRIVATE;
        this.filters = filters;
        this.reportType = ReportTypeEnum.STANDARD;
        this.executionLogs = executionLogs;
        this.isDeleted = false;
        this.audit = audit;
    }

    public Report() {

    }

    public void updateExecutionLog(final String username) throws ReportingServiceException {

        ExecutionLog executionLog;

        if (isEmpty(executionLogs)) {
            executionLog = ExecutionLog.builder().report(this).executedBy(username).build();
            executionLogs.add(executionLog);

        } else {
            executionLog = executionLogs.iterator().next();
            executionLog.setExecutedOn(DateUtils.nowUTC().toDate());
        }
    }

    public boolean isLastPositionSelected() {
        for (Filter filter : filters) {
            if (filter instanceof CommonFilter) {
                PositionSelector positionSelector = ((CommonFilter) filter).getPositionSelector();
                if (positionSelector != null && Position.positions.equals(positionSelector.getPosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void merge(Report incoming) {
        mergeDetails(incoming.details);
        this.isDeleted = incoming.isDeleted;
        this.deletedOn = incoming.deletedOn;
        this.deletedBy = incoming.deletedBy;
        this.visibility = incoming.visibility;
        this.reportType = incoming.reportType;
    }

    public void mergeDetails(ReportDetails reportDetails) {
        this.details.merge(reportDetails);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PrePersist
    private void onCreate() {
        audit.setCreatedOn(DateUtils.nowUTC().toDate());
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Set<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }

    public ReportDetails getDetails() {
        return details;
    }

    public void setDetails(ReportDetails details) {
        this.details = details;
    }

    public VisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityEnum visibility) {
        this.visibility = visibility;
    }

    public ReportTypeEnum getReportType() {
        return reportType;
    }

    public void setReportType(ReportTypeEnum reportType) {
        this.reportType = reportType;
    }
}