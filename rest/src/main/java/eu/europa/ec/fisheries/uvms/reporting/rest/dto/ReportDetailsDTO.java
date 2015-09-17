package eu.europa.ec.fisheries.uvms.reporting.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;

import java.io.Serializable;

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
	
	//@JsonRawValue
	private String filterExpression;
	
	//@JsonRawValue
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
	 * @return the filterExpression
	 */
	public String getFilterExpression() {
		return filterExpression;
	}
	/**
	 * @param filterExpression the filterExpression to set
	 */
	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
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
