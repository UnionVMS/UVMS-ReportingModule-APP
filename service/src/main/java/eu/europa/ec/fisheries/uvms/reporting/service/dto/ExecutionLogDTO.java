package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

public class ExecutionLogDTO implements Serializable {

	public final static String EXECUTED_ON = "executedOn";

	private long id;
	private String executedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date executedOn;

	public ExecutionLogDTO() {
	}

    @Builder
	public ExecutionLogDTO(long id, String executedBy, Date executedOn) {
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
