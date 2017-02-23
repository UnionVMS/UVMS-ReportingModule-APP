/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@ToString
public class DateTime {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("positionSelector")
    private String positionSelector;
    @JsonProperty("positionTypeSelector")
    private String positionTypeSelector;
    @JsonProperty("xValue")
    private Long xValue;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public DateTime() {
    }

    public DateTime(Long id, String positionSelector, String positionTypeSelector, Long xValue, String startDate, String endDate) {
        this.id = id;
        this.positionSelector = positionSelector;
        this.positionTypeSelector = positionTypeSelector;
        this.xValue = xValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("positionSelector")
    public String getPositionSelector() {
        return positionSelector;
    }

    @JsonProperty("positionSelector")
    public void setPositionSelector(String positionSelector) {
        this.positionSelector = positionSelector;
    }

    @JsonProperty("positionTypeSelector")
    public String getPositionTypeSelector() {
        return positionTypeSelector;
    }

    @JsonProperty("positionTypeSelector")
    public void setPositionTypeSelector(String positionTypeSelector) {
        this.positionTypeSelector = positionTypeSelector;
    }

    @JsonProperty("xValue")
    public Long getXValue() {
        return xValue;
    }

    @JsonProperty("xValue")
    public void setXValue(Long xValue) {
        this.xValue = xValue;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}