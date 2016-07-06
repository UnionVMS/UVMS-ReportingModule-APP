/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

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
public class MapConfiguration {

    @JsonProperty("spatialConnectId")
    private long spatialConnectId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public MapConfiguration() {
    }

    /**
     * 
     * @param spatialConnectId
     */
    public MapConfiguration(long spatialConnectId) {
        this.spatialConnectId = spatialConnectId;
    }

    /**
     * 
     * @return
     *     The spatialConnectId
     */
    @JsonProperty("spatialConnectId")
    public long getSpatialConnectId() {
        return spatialConnectId;
    }

    /**
     * 
     * @param spatialConnectId
     *     The spatialConnectId
     */
    @JsonProperty("spatialConnectId")
    public void setSpatialConnectId(long spatialConnectId) {
        this.spatialConnectId = spatialConnectId;
    }

    public MapConfiguration withSpatialConnectId(long spatialConnectId) {
        this.spatialConnectId = spatialConnectId;
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

    public MapConfiguration withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(spatialConnectId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MapConfiguration) == false) {
            return false;
        }
        MapConfiguration rhs = ((MapConfiguration) other);
        return new EqualsBuilder().append(spatialConnectId, rhs.spatialConnectId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}