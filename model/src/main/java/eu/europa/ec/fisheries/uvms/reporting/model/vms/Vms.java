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
public class Vms {

    @JsonProperty("vmsposition")
    @Valid
    private VmsPosition vmsposition;
    @JsonProperty("vmstrack")
    @Valid
    private VmsTrack vmstrack;
    @JsonProperty("vmssegment")
    @Valid
    private VmsSegment vmssegment;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Vms() {
    }

    /**
     * 
     * @param vmsposition
     * @param vmssegment
     * @param tracks
     */
    public Vms(VmsPosition vmsposition, VmsTrack vmstrack, VmsSegment vmssegment) {
        this.vmsposition = vmsposition;
        this.vmstrack = vmstrack;
        this.vmssegment = vmssegment;
    }

    /**
     * 
     * @return
     *     The vmsposition
     */
    @JsonProperty("vmsposition")
    public VmsPosition getVmsposition() {
        return vmsposition;
    }

    /**
     * 
     * @param vmsposition
     *     The vmsposition
     */
    @JsonProperty("vmsposition")
    public void setVmsposition(VmsPosition vmsposition) {
        this.vmsposition = vmsposition;
    }

    public Vms withVmsposition(VmsPosition vmsposition) {
        this.vmsposition = vmsposition;
        return this;
    }

    /**
     * 
     * @return
     *     The tracks
     */
    @JsonProperty("vmstrack")
    public VmsTrack getVmstrack() {
        return vmstrack;
    }

    /**
     * 
     * @param tracks
     *     The tracks
     */
    @JsonProperty("vmstrack")
    public void setVmstrack(VmsTrack tracks) {
        this.vmstrack = tracks;
    }

    public Vms withTracks(VmsTrack vmstrack) {
        this.vmstrack = vmstrack;
        return this;
    }

    /**
     * 
     * @return
     *     The vmssegment
     */
    @JsonProperty("vmssegment")
    public VmsSegment getVmssegment() {
        return vmssegment;
    }

    /**
     * 
     * @param vmssegment
     *     The vmssegment
     */
    @JsonProperty("vmssegment")
    public void setVmssegment(VmsSegment vmssegment) {
        this.vmssegment = vmssegment;
    }

    public Vms withVmssegment(VmsSegment vmssegment) {
        this.vmssegment = vmssegment;
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

    public Vms withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vmsposition).append(vmstrack).append(vmssegment).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vms) == false) {
            return false;
        }
        Vms rhs = ((Vms) other);
        return new EqualsBuilder().append(vmsposition, rhs.vmsposition).append(vmstrack, rhs.vmstrack).append(vmssegment, rhs.vmssegment).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}