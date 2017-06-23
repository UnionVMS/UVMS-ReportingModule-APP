/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by padhyad on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilityPositionsDto implements Serializable{

    @NotNull
    @JsonProperty("popup")
    private VisibilityAttributesDto popup;

    @NotNull
    @JsonProperty("labels")
    private VisibilityAttributesDto labels;

    @NotNull
    @JsonProperty("table")
    private VisibilityAttributesDto table;

    public VisibilityPositionsDto() {}

    public VisibilityPositionsDto(VisibilityAttributesDto popup, VisibilityAttributesDto labels, VisibilityAttributesDto table) {
        this.popup = popup;
        this.labels = labels;
        this.table = table;
    }

    @JsonProperty("popup")
    public VisibilityAttributesDto getPopup() {
        return popup;
    }

    @JsonProperty("popup")
    public void setPopup(VisibilityAttributesDto popup) {
        this.popup = popup;
    }

    @JsonProperty("labels")
    public VisibilityAttributesDto getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(VisibilityAttributesDto labels) {
        this.labels = labels;
    }

    @JsonProperty("table")
    public VisibilityAttributesDto getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(VisibilityAttributesDto table) {
        this.table = table;
    }
}