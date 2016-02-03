
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
public class Vmsposition {

    @JsonProperty("id")
    private long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("movMinSpeed")
    private long movMinSpeed;
    @JsonProperty("movMaxSpeed")
    private long movMaxSpeed;
    @JsonProperty("movType")
    private String movType;
    @JsonProperty("movActivity")
    private String movActivity;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Vmsposition() {
    }

    /**
     * 
     * @param id
     * @param movType
     * @param movActivity
     * @param movMaxSpeed
     * @param type
     * @param movMinSpeed
     */
    public Vmsposition(long id, String type, long movMinSpeed, long movMaxSpeed, String movType, String movActivity) {
        this.id = id;
        this.type = type;
        this.movMinSpeed = movMinSpeed;
        this.movMaxSpeed = movMaxSpeed;
        this.movType = movType;
        this.movActivity = movActivity;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    public Vmsposition withId(long id) {
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

    public Vmsposition withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The movMinSpeed
     */
    @JsonProperty("movMinSpeed")
    public long getMovMinSpeed() {
        return movMinSpeed;
    }

    /**
     * 
     * @param movMinSpeed
     *     The movMinSpeed
     */
    @JsonProperty("movMinSpeed")
    public void setMovMinSpeed(long movMinSpeed) {
        this.movMinSpeed = movMinSpeed;
    }

    public Vmsposition withMovMinSpeed(long movMinSpeed) {
        this.movMinSpeed = movMinSpeed;
        return this;
    }

    /**
     * 
     * @return
     *     The movMaxSpeed
     */
    @JsonProperty("movMaxSpeed")
    public long getMovMaxSpeed() {
        return movMaxSpeed;
    }

    /**
     * 
     * @param movMaxSpeed
     *     The movMaxSpeed
     */
    @JsonProperty("movMaxSpeed")
    public void setMovMaxSpeed(long movMaxSpeed) {
        this.movMaxSpeed = movMaxSpeed;
    }

    public Vmsposition withMovMaxSpeed(long movMaxSpeed) {
        this.movMaxSpeed = movMaxSpeed;
        return this;
    }

    /**
     * 
     * @return
     *     The movType
     */
    @JsonProperty("movType")
    public String getMovType() {
        return movType;
    }

    /**
     * 
     * @param movType
     *     The movType
     */
    @JsonProperty("movType")
    public void setMovType(String movType) {
        this.movType = movType;
    }

    public Vmsposition withMovType(String movType) {
        this.movType = movType;
        return this;
    }

    /**
     * 
     * @return
     *     The movActivity
     */
    @JsonProperty("movActivity")
    public String getMovActivity() {
        return movActivity;
    }

    /**
     * 
     * @param movActivity
     *     The movActivity
     */
    @JsonProperty("movActivity")
    public void setMovActivity(String movActivity) {
        this.movActivity = movActivity;
    }

    public Vmsposition withMovActivity(String movActivity) {
        this.movActivity = movActivity;
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

    public Vmsposition withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(type).append(movMinSpeed).append(movMaxSpeed).append(movType).append(movActivity).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vmsposition) == false) {
            return false;
        }
        Vmsposition rhs = ((Vmsposition) other);
        return new EqualsBuilder().append(id, rhs.id).append(type, rhs.type).append(movMinSpeed, rhs.movMinSpeed).append(movMaxSpeed, rhs.movMaxSpeed).append(movType, rhs.movType).append(movActivity, rhs.movActivity).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
