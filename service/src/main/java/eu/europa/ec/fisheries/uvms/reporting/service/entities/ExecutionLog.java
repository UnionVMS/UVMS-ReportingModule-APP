package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.Builder;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "report_execution_log", uniqueConstraints = @UniqueConstraint(columnNames = "report_id"))
@Builder
public class ExecutionLog implements Serializable {

	private long id;
	private Report report;
	private String executedBy;
	private Date executedOn;

	public ExecutionLog() {
	}

	public ExecutionLog(long id, Report report, String executedBy,
                        Date executedOn) {
		this.id = id;
		this.report = report;
		this.executedBy = executedBy;
		this.executedOn = executedOn;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id", unique = true, nullable = false)
	public Report getReport() {
		return this.report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	@Column(name = "executed_by", nullable = false)
	public String getExecutedBy() {
		return this.executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "executed_on", nullable = false, length = 29)
	public Date getExecutedOn() {
		return this.executedOn;
	}

	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}

}
