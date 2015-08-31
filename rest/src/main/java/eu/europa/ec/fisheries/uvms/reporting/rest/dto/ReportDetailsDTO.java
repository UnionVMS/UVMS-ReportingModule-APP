package eu.europa.ec.fisheries.uvms.reporting.rest.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4610641335256598202L;

	private long id;
	private String name;
	private String desc;
	private VisibilityEnum visibility;
	private long scopeId;
	
	@JsonRawValue
	private String outComponents;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
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

	public long getScopeId() {
		return scopeId;
	}
	public void setScopeId(long scopeId) {
		this.scopeId = scopeId;
	}
	/**
	 * @return the outComponents
	 */
	public String getOutComponents() {
		return outComponents;
	}
	/**
	 * @param outComponents the outComponents to set
	 */
	public void setOutComponents(String outComponents) {
		this.outComponents = outComponents;
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
