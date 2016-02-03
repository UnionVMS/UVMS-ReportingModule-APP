
package eu.europa.ec.fisheries.uvms.reporting.model.vms;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class Tracks {

    @JsonProperty("id")
    private long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("trkMaxTime")
    private long trkMaxTime;
    @JsonProperty("trkMinTime")
    private long trkMinTime;
    @JsonProperty("trkMinDuration")
    private long trkMinDuration;
    @JsonProperty("trkMaxDuration")
    private long trkMaxDuration;
    @JsonProperty("trkMinDistance")
    private long trkMinDistance;
    @JsonProperty("trkMaxDistance")
    private long trkMaxDistance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Tracks() {
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
    public Tracks(long id, String type, long trkMaxTime, long trkMinTime, long trkMinDuration, long trkMaxDuration, long trkMinDistance, long trkMaxDistance) {
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

    public Tracks withId(long id) {
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

    public Tracks withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The trkMaxTime
     */
    @JsonProperty("trkMaxTime")
    public long getTrkMaxTime() {
        return trkMaxTime;
    }

    /**
     * 
     * @param trkMaxTime
     *     The trkMaxTime
     */
    @JsonProperty("trkMaxTime")
    public void setTrkMaxTime(long trkMaxTime) {
        this.trkMaxTime = trkMaxTime;
    }

    public Tracks withTrkMaxTime(long trkMaxTime) {
        this.trkMaxTime = trkMaxTime;
        return this;
    }

    /**
     * 
     * @return
     *     The trkMinTime
     */
    @JsonProperty("trkMinTime")
    public long getTrkMinTime() {
        return trkMinTime;
    }

    /**
     * 
     * @param trkMinTime
     *     The trkMinTime
     */
    @JsonProperty("trkMinTime")
    public void setTrkMinTime(long trkMinTime) {
        this.trkMinTime = trkMinTime;
    }

    public Tracks withTrkMinTime(long trkMinTime) {
        this.trkMinTime = trkMinTime;
        return this;
    }

    /**
     * 
     * @return
     *     The trkMinDuration
     */
    @JsonProperty("trkMinDuration")
    public long getTrkMinDuration() {
        return trkMinDuration;
    }

    /**
     * 
     * @param trkMinDuration
     *     The trkMinDuration
     */
    @JsonProperty("trkMinDuration")
    public void setTrkMinDuration(long trkMinDuration) {
        this.trkMinDuration = trkMinDuration;
    }

    public Tracks withTrkMinDuration(long trkMinDuration) {
        this.trkMinDuration = trkMinDuration;
        return this;
    }

    /**
     * 
     * @return
     *     The trkMaxDuration
     */
    @JsonProperty("trkMaxDuration")
    public long getTrkMaxDuration() {
        return trkMaxDuration;
    }

    /**
     * 
     * @param trkMaxDuration
     *     The trkMaxDuration
     */
    @JsonProperty("trkMaxDuration")
    public void setTrkMaxDuration(long trkMaxDuration) {
        this.trkMaxDuration = trkMaxDuration;
    }

    public Tracks withTrkMaxDuration(long trkMaxDuration) {
        this.trkMaxDuration = trkMaxDuration;
        return this;
    }

    /**
     * 
     * @return
     *     The trkMinDistance
     */
    @JsonProperty("trkMinDistance")
    public long getTrkMinDistance() {
        return trkMinDistance;
    }

    /**
     * 
     * @param trkMinDistance
     *     The trkMinDistance
     */
    @JsonProperty("trkMinDistance")
    public void setTrkMinDistance(long trkMinDistance) {
        this.trkMinDistance = trkMinDistance;
    }

    public Tracks withTrkMinDistance(long trkMinDistance) {
        this.trkMinDistance = trkMinDistance;
        return this;
    }

    /**
     * 
     * @return
     *     The trkMaxDistance
     */
    @JsonProperty("trkMaxDistance")
    public long getTrkMaxDistance() {
        return trkMaxDistance;
    }

    /**
     * 
     * @param trkMaxDistance
     *     The trkMaxDistance
     */
    @JsonProperty("trkMaxDistance")
    public void setTrkMaxDistance(long trkMaxDistance) {
        this.trkMaxDistance = trkMaxDistance;
    }

    public Tracks withTrkMaxDistance(long trkMaxDistance) {
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

    public Tracks withAdditionalProperty(String name, Object value) {
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
        if ((other instanceof Tracks) == false) {
            return false;
        }
        Tracks rhs = ((Tracks) other);
        return new EqualsBuilder().append(id, rhs.id).append(type, rhs.type).append(trkMaxTime, rhs.trkMaxTime).append(trkMinTime, rhs.trkMinTime).append(trkMinDuration, rhs.trkMinDuration).append(trkMaxDuration, rhs.trkMaxDuration).append(trkMinDistance, rhs.trkMinDistance).append(trkMaxDistance, rhs.trkMaxDistance).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
