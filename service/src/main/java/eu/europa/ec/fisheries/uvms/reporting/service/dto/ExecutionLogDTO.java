/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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