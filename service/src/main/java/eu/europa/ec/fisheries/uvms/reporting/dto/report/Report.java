/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.dto.report;

import eu.europa.ec.fisheries.uvms.reporting.dto.MapConfiguration;
import eu.europa.ec.fisheries.uvms.reporting.dto.FilterExpression;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@EqualsAndHashCode
public class Report {

    private Long id;
    private String name;
    private String desc;
    private String visibility;
    private String reportType;
    private String createdOn;
    private Object executedOn;
    private String createdBy;
    private boolean withMap;

    @JsonProperty("mapConfiguration")
    @Valid
    private MapConfiguration mapConfiguration;

    @JsonProperty("filterExpression")
    @Valid
    private FilterExpression filterExpression;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Report() {
    }

    public Report(Long id, String name, String desc, String visibility, String createdOn, Object executedOn, String createdBy, boolean withMap, MapConfiguration mapConfiguration, FilterExpression filterExpression) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.visibility = visibility;
        this.createdOn = createdOn;
        this.executedOn = executedOn;
        this.createdBy = createdBy;
        this.withMap = withMap;
        this.mapConfiguration = mapConfiguration;
        this.filterExpression = filterExpression;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public Report withId(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Report withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Report withDesc(String desc) {
        this.desc = desc;
        return this;
    }

    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Report withVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    @JsonProperty("createdOn")
    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("createdOn")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Report withCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @JsonProperty("executedOn")
    public Object getExecutedOn() {
        return executedOn;
    }

    @JsonProperty("executedOn")
    public void setExecutedOn(Object executedOn) {
        this.executedOn = executedOn;
    }

    public Report withExecutedOn(Object executedOn) {
        this.executedOn = executedOn;
        return this;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Report withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonProperty("withMap")
    public boolean isWithMap() {
        return withMap;
    }

    @JsonProperty("withMap")
    public void setWithMap(boolean withMap) {
        this.withMap = withMap;
    }

    public Report withWithMap(boolean withMap) {
        this.withMap = withMap;
        return this;
    }

    @JsonProperty("mapConfiguration")
    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    @JsonProperty("mapConfiguration")
    public void setMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    public Report withMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
        return this;
    }

    @JsonProperty("filterExpression")
    public FilterExpression getFilterExpression() {
        return filterExpression;
    }

    @JsonProperty("filterExpression")
    public void setFilterExpression(FilterExpression filterExpression) {
        this.filterExpression = filterExpression;
    }

    public Report withFilterExpression(FilterExpression filterExpression) {
        this.filterExpression = filterExpression;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Report withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}