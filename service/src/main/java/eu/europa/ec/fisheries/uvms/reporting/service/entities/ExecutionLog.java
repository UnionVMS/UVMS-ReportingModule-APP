package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.Builder;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "execution_log", schema = "reporting",
        uniqueConstraints = @UniqueConstraint(columnNames = {"report_id", "executed_by"}))
public class ExecutionLog implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "report_id")
    @Valid
	private Report report;

    @Column(name = "executed_by")
    @NotNull
    private String executedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "executed_on")
    @NotNull
	private Date executedOn;

	public ExecutionLog() {
	}

    @Builder
	public ExecutionLog(long id, Report report, String executedBy) {
		this.id = id;
		this.report = report;
		this.executedBy = executedBy;
		this.executedOn = new Date();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Report getReport() {
		return this.report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getExecutedBy() {
		return this.executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	public Date getExecutedOn() {
		return this.executedOn;
	}

	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}

}
