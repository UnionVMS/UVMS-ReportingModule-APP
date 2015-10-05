package eu.europa.ec.fisheries.uvms.reporting.model;

// Generated Aug 6, 2015 11:44:29 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FIXME unify the Report DTO usage with the ReportServiceBean
 */
public class Report implements java.io.Serializable {

	private long id;
	private String name;
	private String description;
	private String filterExpression;
	private String outComponents;
	private String scopeName;
	private String createdBy;
	private Date createdOn;
	private VisibilityEnum visibility;
	private boolean isDeleted;
	private Date deletedOn;
	private String deletedBy;
	private Set<ReportExecutionLog> reportExecutionLogs = new HashSet<ReportExecutionLog>(
			0);

	public Report() {
	}

	public Report(long id, String name, String filterExpression,
			String outComponents, boolean isActive, String scopeName,
			String createdBy, Date createdOn, VisibilityEnum visibility,
			boolean isDeleted) {
		this.id = id;
		this.name = name;
		this.filterExpression = filterExpression;
		this.outComponents = outComponents;
		this.scopeName = scopeName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.setVisibility(visibility);
		this.isDeleted = isDeleted;
	}

	public Report(long id, String name, String description,
			String filterExpression, String outComponents, boolean isActive,
			String scopeName, String createdBy, Date createdOn, VisibilityEnum visibility,
			boolean isDeleted, Date deletedOn, String deletedBy,
			Set<ReportExecutionLog> reportExecutionLogs) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.filterExpression = filterExpression;
		this.outComponents = outComponents;
		this.scopeName = scopeName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.setVisibility(visibility);
		this.isDeleted = isDeleted;
		this.deletedOn = deletedOn;
		this.deletedBy = deletedBy;
		this.reportExecutionLogs = reportExecutionLogs;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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

	public String getFilterExpression() {
		return this.filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
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

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
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

	public Set<ReportExecutionLog> getReportExecutionLogs() {
		return this.reportExecutionLogs;
	}

	public void setReportExecutionLogs(
			Set<ReportExecutionLog> reportExecutionLogs) {
		this.reportExecutionLogs = reportExecutionLogs;
	}

	/**
	 * @return the visibility
	 */
	public VisibilityEnum getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(VisibilityEnum visibility) {
		this.visibility = visibility;
	}

}
