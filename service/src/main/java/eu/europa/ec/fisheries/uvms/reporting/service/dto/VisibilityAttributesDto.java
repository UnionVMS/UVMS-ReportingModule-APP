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

import java.io.Serializable;
import java.util.List;

/**
 * Created by padhyad on 1/28/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VisibilityAttributesDto implements Serializable{

    private Boolean isAttributeVisible;

    @JsonProperty("order")
    private List<String> order;

    @JsonProperty("values")
    private List<String> values;

    public VisibilityAttributesDto(){}

    public VisibilityAttributesDto(List<String> order, List<String> values, Boolean isAttributeVisible) {
        this.order = order;
        this.values = values;
        this.isAttributeVisible = isAttributeVisible;
    }

    @JsonProperty("order")
    public List<String> getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(List<String> order) {
        this.order = order;
    }

    @JsonProperty("values")
    public List<String> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<String> values) {
        this.values = values;
    }

    @JsonProperty("isAttributeVisible")
    public Boolean isAttributeVisible() {
        return isAttributeVisible;
    }

    @JsonProperty("isAttributeVisible")
    public void setIsAttributeVisible(Boolean isAttributeVisible) {
        this.isAttributeVisible = isAttributeVisible;
    }
}