package eu.europa.ec.fisheries.uvms.reporting.rest.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4610641335256598202L;

	private long id;
	private String name;
	private String desc;
	private boolean isShared;
	private long scopeId;
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
	public boolean getIsShared() {
		return isShared;
	}
	public void setIsShared(boolean isShared) {
		this.isShared = isShared;
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
	
	
	
}
