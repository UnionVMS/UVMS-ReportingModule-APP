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
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VmsTrack {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("trkMaxTime")
    private Float trkMaxTime;
    @JsonProperty("trkMinTime")
    private Float trkMinTime;
    @JsonProperty("trkMinDuration")
    private Float trkMinDuration;
    @JsonProperty("trkMaxDuration")
    private Float trkMaxDuration;
    @JsonProperty("trkMinDistance")
    private Float trkMinDistance;
    @JsonProperty("trkMaxDistance")
    private Float trkMaxDistance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public VmsTrack() {
    }

    /**
     *
     * @param id
     * @param trkMinDistance
     * @param trkMaxDuration
     * @param trkMinDuration
     * @param type
     * @param trkMaxTime
     * @param trkMinTime
     * @param trkMaxDistance
     */
    public VmsTrack(Long id, String type, Float trkMaxTime, Float trkMinTime, Float trkMinDuration, Float trkMaxDuration, Float trkMinDistance, Float trkMaxDistance) {
        this.id = id;
        this.type = type;
        this.trkMaxTime = trkMaxTime;
        this.trkMinTime = trkMinTime;
        this.trkMinDuration = trkMinDuration;
        this.trkMaxDuration = trkMaxDuration;
        this.trkMinDistance = trkMinDistance;
        this.trkMaxDistance = trkMaxDistance;
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

    public VmsTrack withId(Long id) {
        this.id = id;
        return this;
    }

    /**
     *
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public VmsTrack withType(String type) {
        this.type = type;
        return this;
    }

    /**
     *
     * @return
     *     The trkMaxTime
     */
    @JsonProperty("trkMaxTime")
    public Float getTrkMaxTime() {
        return trkMaxTime;
    }

    /**
     *
     * @param trkMaxTime
     *     The trkMaxTime
     */
    @JsonProperty("trkMaxTime")
    public void setTrkMaxTime(Float trkMaxTime) {
        this.trkMaxTime = trkMaxTime;
    }

    public VmsTrack withTrkMaxTime(Float trkMaxTime) {
        this.trkMaxTime = trkMaxTime;
        return this;
    }

    /**
     *
     * @return
     *     The trkMinTime
     */
    @JsonProperty("trkMinTime")
    public Float getTrkMinTime() {
        return trkMinTime;
    }

    /**
     *
     * @param trkMinTime
     *     The trkMinTime
     */
    @JsonProperty("trkMinTime")
    public void setTrkMinTime(Float trkMinTime) {
        this.trkMinTime = trkMinTime;
    }

    public VmsTrack withTrkMinTime(Float trkMinTime) {
        this.trkMinTime = trkMinTime;
        return this;
    }

    /**
     *
     * @return
     *     The trkMinDuration
     */
    @JsonProperty("trkMinDuration")
    public Float getTrkMinDuration() {
        return trkMinDuration;
    }

    /**
     *
     * @param trkMinDuration
     *     The trkMinDuration
     */
    @JsonProperty("trkMinDuration")
    public void setTrkMinDuration(Float trkMinDuration) {
        this.trkMinDuration = trkMinDuration;
    }

    public VmsTrack withTrkMinDuration(Float trkMinDuration) {
        this.trkMinDuration = trkMinDuration;
        return this;
    }

    /**
     *
     * @return
     *     The trkMaxDuration
     */
    @JsonProperty("trkMaxDuration")
    public Float getTrkMaxDuration() {
        return trkMaxDuration;
    }

    /**
     *
     * @param trkMaxDuration
     *     The trkMaxDuration
     */
    @JsonProperty("trkMaxDuration")
    public void setTrkMaxDuration(Float trkMaxDuration) {
        this.trkMaxDuration = trkMaxDuration;
    }

    public VmsTrack withTrkMaxDuration(Float trkMaxDuration) {
        this.trkMaxDuration = trkMaxDuration;
        return this;
    }

    /**
     *
     * @return
     *     The trkMinDistance
     */
    @JsonProperty("trkMinDistance")
    public Float getTrkMinDistance() {
        return trkMinDistance;
    }

    /**
     *
     * @param trkMinDistance
     *     The trkMinDistance
     */
    @JsonProperty("trkMinDistance")
    public void setTrkMinDistance(Float trkMinDistance) {
        this.trkMinDistance = trkMinDistance;
    }

    public VmsTrack withTrkMinDistance(Float trkMinDistance) {
        this.trkMinDistance = trkMinDistance;
        return this;
    }

    /**
     *
     * @return
     *     The trkMaxDistance
     */
    @JsonProperty("trkMaxDistance")
    public Float getTrkMaxDistance() {
        return trkMaxDistance;
    }

    /**
     *
     * @param trkMaxDistance
     *     The trkMaxDistance
     */
    @JsonProperty("trkMaxDistance")
    public void setTrkMaxDistance(Float trkMaxDistance) {
        this.trkMaxDistance = trkMaxDistance;
    }

    public VmsTrack withTrkMaxDistance(Float trkMaxDistance) {
        this.trkMaxDistance = trkMaxDistance;
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

    public VmsTrack withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(type).append(trkMaxTime).append(trkMinTime).append(trkMinDuration).append(trkMaxDuration).append(trkMinDistance).append(trkMaxDistance).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VmsTrack) == false) {
            return false;
        }
        VmsTrack rhs = ((VmsTrack) other);
        return new EqualsBuilder().append(id, rhs.id).append(type, rhs.type).append(trkMaxTime, rhs.trkMaxTime).append(trkMinTime, rhs.trkMinTime).append(trkMinDuration, rhs.trkMinDuration).append(trkMaxDuration, rhs.trkMaxDuration).append(trkMinDistance, rhs.trkMinDistance).append(trkMaxDistance, rhs.trkMaxDistance).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}