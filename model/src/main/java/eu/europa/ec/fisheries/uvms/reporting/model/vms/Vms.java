
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
public class Vms {

    @JsonProperty("vmsposition")
    @Valid
    private Vmsposition vmsposition;
    @JsonProperty("vmstrack")
    @Valid
    private VmsTrack vmstrack;
    @JsonProperty("vmssegment")
    @Valid
    private Vmssegment vmssegment;
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
    public Vms(Vmsposition vmsposition, VmsTrack vmstrack, Vmssegment vmssegment) {
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
    public Vmsposition getVmsposition() {
        return vmsposition;
    }

    /**
     * 
     * @param vmsposition
     *     The vmsposition
     */
    @JsonProperty("vmsposition")
    public void setVmsposition(Vmsposition vmsposition) {
        this.vmsposition = vmsposition;
    }

    public Vms withVmsposition(Vmsposition vmsposition) {
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
    public Vmssegment getVmssegment() {
        return vmssegment;
    }

    /**
     * 
     * @param vmssegment
     *     The vmssegment
     */
    @JsonProperty("vmssegment")
    public void setVmssegment(Vmssegment vmssegment) {
        this.vmssegment = vmssegment;
    }

    public Vms withVmssegment(Vmssegment vmssegment) {
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
