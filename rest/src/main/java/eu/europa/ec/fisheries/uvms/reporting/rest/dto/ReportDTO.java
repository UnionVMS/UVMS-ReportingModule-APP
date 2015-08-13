package eu.europa.ec.fisheries.uvms.reporting.rest.dto;

// Generated Aug 6, 2015 11:44:29 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 */
public class ReportDTO implements java.io.Serializable {

	private long id;
	private String name;
	private String desc;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date created_on;
	private boolean is_shared;
	
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



	public Date getCreated_on() {
		return created_on;
	}



	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
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



	public boolean isIs_shared() {
		return is_shared;
	}



	public void setIs_shared(boolean is_shared) {
		this.is_shared = is_shared;
	}

	

}
