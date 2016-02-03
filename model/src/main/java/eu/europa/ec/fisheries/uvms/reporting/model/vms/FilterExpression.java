
package eu.europa.ec.fisheries.uvms.reporting.model.vms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
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
public class FilterExpression {

    @JsonProperty("common")
    @Valid
    private Common common;
    @JsonProperty("vms")
    @Valid
    private Vms vms;
    @JsonProperty("areas")
    @Valid
    private List<Area> areas = new ArrayList<Area>();
    @JsonProperty("assets")
    @Valid
    private List<Asset> assets = new ArrayList<Asset>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterExpression() {
    }

    /**
     * 
     * @param assets
     * @param common
     * @param areas
     * @param vms
     */
    public FilterExpression(Common common, Vms vms, List<Area> areas, List<Asset> assets) {
        this.common = common;
        this.vms = vms;
        this.areas = areas;
        this.assets = assets;
    }

    /**
     * 
     * @return
     *     The common
     */
    @JsonProperty("common")
    public Common getCommon() {
        return common;
    }

    /**
     * 
     * @param common
     *     The common
     */
    @JsonProperty("common")
    public void setCommon(Common common) {
        this.common = common;
    }

    public FilterExpression withCommon(Common common) {
        this.common = common;
        return this;
    }

    /**
     * 
     * @return
     *     The vms
     */
    @JsonProperty("vms")
    public Vms getVms() {
        return vms;
    }

    /**
     * 
     * @param vms
     *     The vms
     */
    @JsonProperty("vms")
    public void setVms(Vms vms) {
        this.vms = vms;
    }

    public FilterExpression withVms(Vms vms) {
        this.vms = vms;
        return this;
    }

    /**
     * 
     * @return
     *     The areas
     */
    @JsonProperty("areas")
    public List<Area> getAreas() {
        return areas;
    }

    /**
     * 
     * @param areas
     *     The areas
     */
    @JsonProperty("areas")
    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public FilterExpression withAreas(List<Area> areas) {
        this.areas = areas;
        return this;
    }

    /**
     * 
     * @return
     *     The assets
     */
    @JsonProperty("assets")
    public List<Asset> getAssets() {
        return assets;
    }

    /**
     * 
     * @param assets
     *     The assets
     */
    @JsonProperty("assets")
    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public FilterExpression withAssets(List<Asset> assets) {
        this.assets = assets;
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

    public FilterExpression withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(common).append(vms).append(areas).append(assets).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FilterExpression) == false) {
            return false;
        }
        FilterExpression rhs = ((FilterExpression) other);
        return new EqualsBuilder().append(common, rhs.common).append(vms, rhs.vms).append(areas, rhs.areas).append(assets, rhs.assets).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
