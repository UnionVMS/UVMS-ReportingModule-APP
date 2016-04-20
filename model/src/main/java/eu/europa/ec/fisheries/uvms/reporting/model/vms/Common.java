
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
public class Common {

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

    /**
     * No args constructor for use in serialization
     * 
     */
    public Common() {
    }

    /**
     * 
     * @param id
     * @param startDate
     * @param positionSelector
     * @param endDate
     * @param xValue
     * @param positionTypeSelector
     */
    public Common(Long id, String positionSelector, String positionTypeSelector, Long xValue, String startDate, String endDate) {
        this.id = id;
        this.positionSelector = positionSelector;
        this.positionTypeSelector = positionTypeSelector;
        this.xValue = xValue;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Common withId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The positionSelector
     */
    @JsonProperty("positionSelector")
    public String getPositionSelector() {
        return positionSelector;
    }

    /**
     * 
     * @param positionSelector
     *     The positionSelector
     */
    @JsonProperty("positionSelector")
    public void setPositionSelector(String positionSelector) {
        this.positionSelector = positionSelector;
    }

    public Common withPositionSelector(String positionSelector) {
        this.positionSelector = positionSelector;
        return this;
    }

    /**
     * 
     * @return
     *     The positionTypeSelector
     */
    @JsonProperty("positionTypeSelector")
    public String getPositionTypeSelector() {
        return positionTypeSelector;
    }

    /**
     * 
     * @param positionTypeSelector
     *     The positionTypeSelector
     */
    @JsonProperty("positionTypeSelector")
    public void setPositionTypeSelector(String positionTypeSelector) {
        this.positionTypeSelector = positionTypeSelector;
    }

    public Common withPositionTypeSelector(String positionTypeSelector) {
        this.positionTypeSelector = positionTypeSelector;
        return this;
    }

    /**
     * 
     * @return
     *     The xValue
     */
    @JsonProperty("xValue")
    public Long getXValue() {
        return xValue;
    }

    /**
     * 
     * @param xValue
     *     The xValue
     */
    @JsonProperty("xValue")
    public void setXValue(Long xValue) {
        this.xValue = xValue;
    }

    public Common withXValue(Long xValue) {
        this.xValue = xValue;
        return this;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Common withStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Common withEndDate(String endDate) {
        this.endDate = endDate;
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

    public Common withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(positionSelector).append(positionTypeSelector).append(xValue).append(startDate).append(endDate).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Common) == false) {
            return false;
        }
        Common rhs = ((Common) other);
        return new EqualsBuilder().append(id, rhs.id).append(positionSelector, rhs.positionSelector).append(positionTypeSelector, rhs.positionTypeSelector).append(xValue, rhs.xValue).append(startDate, rhs.startDate).append(endDate, rhs.endDate).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
