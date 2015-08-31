package eu.europa.ec.fisheries.uvms.reporting.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.json.CustomDateSerializer;
import eu.europa.ec.fisheries.uvms.reporting.rest.json.VisibilitySerializer;

/**
 */
public class ReportDTO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7155759726599178091L;
	
	private long id;
	private String name;
	private String desc;
	
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date createdOn;
	private String createdBy;
	private VisibilityEnum visibility;
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date lastExecTime;
	
	//authorization properties
	private boolean shareable;
	private boolean editable;
	private boolean deletable;

	public ReportDTO() {
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



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}



	public Date getCreatedOn() {
		return createdOn;
	}



	public void setCreatedOn(Date created_on) {
		this.createdOn = created_on;
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



	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}



	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	/**
	 * @return the lastExecTime
	 */
	public Date getLastExecTime() {
		return lastExecTime;
	}



	/**
	 * @param lastExecTime the lastExecTime to set
	 */
	public void setLastExecTime(Date lastExecTime) {
		this.lastExecTime = lastExecTime;
	}




}
