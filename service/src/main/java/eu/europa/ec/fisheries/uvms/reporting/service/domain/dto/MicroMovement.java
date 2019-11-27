package eu.europa.ec.fisheries.uvms.reporting.service.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.uvms.movement.model.MovementInstantDeserializer;
import eu.europa.ec.fisheries.uvms.movement.service.entity.Movement;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class MicroMovement implements Serializable {

    private MovementPoint location;
    private Double heading;
    private String guid;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = MovementInstantDeserializer.class)
    private Instant timestamp;
    private Double speed;
    private MovementSourceType source;

    public MicroMovement() {
    }

    public MicroMovement(Movement movement) {
        Point point = movement.getLocation();
        this.location = new MovementPoint();
        this.location.setLatitude(point.getY());
        this.location.setLongitude(point.getX());
        this.heading = movement.getHeading().doubleValue();
        this.guid = movement.getId().toString();
        this.timestamp = movement.getTimestamp();
        this.speed = movement.getSpeed() == null ? null : movement.getSpeed().doubleValue();
    }

    public MicroMovement(Geometry geo, Float heading, UUID guid, Instant timestamp, Float speed, MovementSourceType source) {
        Point point = (Point)geo;
        this.location = new MovementPoint();
        this.location.setLatitude(point.getY());
        this.location.setLongitude(point.getX());
        this.heading = heading.doubleValue();
        this.guid = guid.toString();
        this.timestamp = timestamp;
        this.speed = speed == null ? null : speed.doubleValue();
        this.source = source;
    }

    public MovementPoint getLocation() {
        return this.location;
    }

    public void setLocation(MovementPoint location) {
        this.location = location;
    }

    public Double getHeading() {
        return this.heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public MovementSourceType getSource() {
        return this.source;
    }

    public void setSource(MovementSourceType source) {
        this.source = source;
    }
}
