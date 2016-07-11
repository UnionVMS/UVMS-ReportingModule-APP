/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.model.vms;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class Report {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("visibility")
    private String visibility;
    @JsonProperty("createdOn")
    private String createdOn;
    @JsonProperty("executedOn")
    private Object executedOn;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("withMap")
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

    /**
     * 
     * @param executedOn
     * @param createdOn
     * @param id
     * @param desc
     * @param mapConfiguration
     * @param createdBy
     * @param visibility
     * @param filterExpression
     * @param name
     * @param withMap
     */
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

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public Report withId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Report withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The desc
     */
    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    /**
     * 
     * @param desc
     *     The desc
     */
    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Report withDesc(String desc) {
        this.desc = desc;
        return this;
    }

    /**
     * 
     * @return
     *     The visibility
     */
    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    /**
     * 
     * @param visibility
     *     The visibility
     */
    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Report withVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    /**
     * 
     * @return
     *     The createdOn
     */
    @JsonProperty("createdOn")
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * 
     * @param createdOn
     *     The createdOn
     */
    @JsonProperty("createdOn")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Report withCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    /**
     * 
     * @return
     *     The executedOn
     */
    @JsonProperty("executedOn")
    public Object getExecutedOn() {
        return executedOn;
    }

    /**
     * 
     * @param executedOn
     *     The executedOn
     */
    @JsonProperty("executedOn")
    public void setExecutedOn(Object executedOn) {
        this.executedOn = executedOn;
    }

    public Report withExecutedOn(Object executedOn) {
        this.executedOn = executedOn;
        return this;
    }

    /**
     * 
     * @return
     *     The createdBy
     */
    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 
     * @param createdBy
     *     The createdBy
     */
    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Report withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * 
     * @return
     *     The withMap
     */
    @JsonProperty("withMap")
    public boolean isWithMap() {
        return withMap;
    }

    /**
     * 
     * @param withMap
     *     The withMap
     */
    @JsonProperty("withMap")
    public void setWithMap(boolean withMap) {
        this.withMap = withMap;
    }

    public Report withWithMap(boolean withMap) {
        this.withMap = withMap;
        return this;
    }

    /**
     * 
     * @return
     *     The mapConfiguration
     */
    @JsonProperty("mapConfiguration")
    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    /**
     * 
     * @param mapConfiguration
     *     The mapConfiguration
     */
    @JsonProperty("mapConfiguration")
    public void setMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    public Report withMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
        return this;
    }

    /**
     * 
     * @return
     *     The filterExpression
     */
    @JsonProperty("filterExpression")
    public FilterExpression getFilterExpression() {
        return filterExpression;
    }

    /**
     * 
     * @param filterExpression
     *     The filterExpression
     */
    @JsonProperty("filterExpression")
    public void setFilterExpression(FilterExpression filterExpression) {
        this.filterExpression = filterExpression;
    }

    public Report withFilterExpression(FilterExpression filterExpression) {
        this.filterExpression = filterExpression;
        return this;
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

    public Report withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(desc).append(visibility).append(createdOn).append(executedOn).append(createdBy).append(withMap).append(mapConfiguration).append(filterExpression).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Report) == false) {
            return false;
        }
        Report rhs = ((Report) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(desc, rhs.desc).append(visibility, rhs.visibility).append(createdOn, rhs.createdOn).append(executedOn, rhs.executedOn).append(createdBy, rhs.createdBy).append(withMap, rhs.withMap).append(mapConfiguration, rhs.mapConfiguration).append(filterExpression, rhs.filterExpression).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}