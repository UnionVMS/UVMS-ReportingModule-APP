
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
public class Vmssegment {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("segMinSpeed")
    private Float segMinSpeed;
    @JsonProperty("segMaxSpeed")
    private Float segMaxSpeed;
    @JsonProperty("segMaxDuration")
    private Float segMaxDuration;
    @JsonProperty("segMinDuration")
    private Float segMinDuration;
    @JsonProperty("segCategory")
    private String segCategory;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Vmssegment() {
    }

    /**
     * 
     * @param id
     * @param segMinDuration
     * @param segMinSpeed
     * @param segCategory
     * @param segMaxSpeed
     * @param segMaxDuration
     * @param type
     */
    public Vmssegment(Long id, String type, Float segMinSpeed, Float segMaxSpeed, Float segMaxDuration, Float segMinDuration, String segCategory) {
        this.id = id;
        this.type = type;
        this.segMinSpeed = segMinSpeed;
        this.segMaxSpeed = segMaxSpeed;
        this.segMaxDuration = segMaxDuration;
        this.segMinDuration = segMinDuration;
        this.segCategory = segCategory;
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

    public Vmssegment withId(Long id) {
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

    public Vmssegment withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The segMinSpeed
     */
    @JsonProperty("segMinSpeed")
    public Float getSegMinSpeed() {
        return segMinSpeed;
    }

    /**
     * 
     * @param segMinSpeed
     *     The segMinSpeed
     */
    @JsonProperty("segMinSpeed")
    public void setSegMinSpeed(Float segMinSpeed) {
        this.segMinSpeed = segMinSpeed;
    }

    public Vmssegment withSegMinSpeed(Float segMinSpeed) {
        this.segMinSpeed = segMinSpeed;
        return this;
    }

    /**
     * 
     * @return
     *     The segMaxSpeed
     */
    @JsonProperty("segMaxSpeed")
    public Float getSegMaxSpeed() {
        return segMaxSpeed;
    }

    /**
     * 
     * @param segMaxSpeed
     *     The segMaxSpeed
     */
    @JsonProperty("segMaxSpeed")
    public void setSegMaxSpeed(Float segMaxSpeed) {
        this.segMaxSpeed = segMaxSpeed;
    }

    public Vmssegment withSegMaxSpeed(Float segMaxSpeed) {
        this.segMaxSpeed = segMaxSpeed;
        return this;
    }

    /**
     * 
     * @return
     *     The segMaxDuration
     */
    @JsonProperty("segMaxDuration")
    public Float getSegMaxDuration() {
        return segMaxDuration;
    }

    /**
     * 
     * @param segMaxDuration
     *     The segMaxDuration
     */
    @JsonProperty("segMaxDuration")
    public void setSegMaxDuration(Float segMaxDuration) {
        this.segMaxDuration = segMaxDuration;
    }

    public Vmssegment withSegMaxDuration(Float segMaxDuration) {
        this.segMaxDuration = segMaxDuration;
        return this;
    }

    /**
     * 
     * @return
     *     The segMinDuration
     */
    @JsonProperty("segMinDuration")
    public Float getSegMinDuration() { // TODO chnage to float
        return segMinDuration;
    }

    /**
     * 
     * @param segMinDuration
     *     The segMinDuration
     */
    @JsonProperty("segMinDuration")
    public void setSegMinDuration(Float segMinDuration) {
        this.segMinDuration = segMinDuration;
    }

    public Vmssegment withSegMinDuration(Float segMinDuration) {
        this.segMinDuration = segMinDuration;
        return this;
    }

    /**
     * 
     * @return
     *     The segCategory
     */
    @JsonProperty("segCategory")
    public String getSegCategory() {
        return segCategory;
    }

    /**
     * 
     * @param segCategory
     *     The segCategory
     */
    @JsonProperty("segCategory")
    public void setSegCategory(String segCategory) {
        this.segCategory = segCategory;
    }

    public Vmssegment withSegCategory(String segCategory) {
        this.segCategory = segCategory;
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

    public Vmssegment withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(type).append(segMinSpeed).append(segMaxSpeed).append(segMaxDuration).append(segMinDuration).append(segCategory).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vmssegment) == false) {
            return false;
        }
        Vmssegment rhs = ((Vmssegment) other);
        return new EqualsBuilder().append(id, rhs.id).append(type, rhs.type).append(segMinSpeed, rhs.segMinSpeed).append(segMaxSpeed, rhs.segMaxSpeed).append(segMaxDuration, rhs.segMaxDuration).append(segMinDuration, rhs.segMinDuration).append(segCategory, rhs.segCategory).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
