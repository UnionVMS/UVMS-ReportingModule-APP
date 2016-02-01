package eu.europa.ec.fisheries.uvms.reporting.model;

// Generated Aug 6, 2015 11:44:29 AM by Hibernate Tools 4.3.1

import java.util.Date;

/**
 */
public class ReportExecutionLog implements java.io.Serializable {

	private long id;
	private String executedBy;
	private Date executedOn;

	public ReportExecutionLog() {
	}

	public ReportExecutionLog(long id, String executedBy,
			Date executedOn) {
		this.id = id;
		this.executedBy = executedBy;
		this.executedOn = executedOn;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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
