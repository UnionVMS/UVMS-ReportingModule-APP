package eu.europa.ec.fisheries.uvms.reporting.service.dto.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 3/25/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmMovement {

    @JsonProperty("id")
    private String movementId;

    @JsonProperty("x")
    private String xCoordinate;

    @JsonProperty("y")
    private String yCoordinate;

    @JsonProperty("id")
    public String getMovementId() {
        return movementId;
    }

    @JsonProperty("id")
    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    @JsonProperty("x")
    public String getxCoordinate() {
        return xCoordinate;
    }

    @JsonProperty("x")
    public void setxCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    @JsonProperty("y")
    public String getyCoordinate() {
        return yCoordinate;
    }

    @JsonProperty("y")
    public void setyCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
