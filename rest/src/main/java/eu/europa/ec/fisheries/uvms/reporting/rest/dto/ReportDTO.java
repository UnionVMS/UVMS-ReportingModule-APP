package eu.europa.ec.fisheries.uvms.reporting.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.europa.ec.fisheries.uvms.reporting.rest.util.CustomDateSerializer;

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
	
	@JsonSerialize(contentUsing = CustomDateSerializer.class)
//	@JsonProperty("createdOn")
	private Date createdOn;
	private boolean isShared;
	
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



	public boolean getIsShared() {
		return isShared;
	}



	public void setIsShared(boolean is_shared) {
		this.isShared = is_shared;
	}

	

}
