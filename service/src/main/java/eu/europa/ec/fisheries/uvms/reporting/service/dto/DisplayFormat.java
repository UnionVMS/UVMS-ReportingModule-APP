package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DisplayFormat {

    @JsonProperty("speedUnit")
    private VelocityType velocityType;
    @JsonProperty("distanceUnit")
    private LengthType lengthType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public DisplayFormat() {
    }

    /**
     * 
     * @param velocityType
     * @param lengthType
     */
    public DisplayFormat(VelocityType velocityType, LengthType lengthType) {
        this.velocityType = velocityType;
        this.lengthType = lengthType;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
