package eu.europa.ec.fisheries.uvms.reporting.service.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.Instant;

public class MicroMovementDto {

    private MovementPointDto location;   //vivid solution point causes infinite json recursion ;(

    private Double heading;

    private String guid;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = IncidentInstantDeserializer.class)
    private Instant timestamp;

    private Double speed;

    private MovementSourceType source;

    public MovementPointDto getLocation() {
        return location;
    }

    public void setLocation(MovementPointDto location) {
        this.location = location;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public MovementSourceType getSource() {
        return source;
    }

    public void setSource(MovementSourceType source) {
        this.source = source;
    }
}
