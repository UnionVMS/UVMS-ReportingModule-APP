package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "speedUnit",
    "distanceUnit"
})
@Data
public class DisplayFormat {

    @JsonProperty("speedUnit")
    private VelocityType velocityType;
    @JsonProperty("distanceUnit")
    private LengthType lengthType;

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
}
