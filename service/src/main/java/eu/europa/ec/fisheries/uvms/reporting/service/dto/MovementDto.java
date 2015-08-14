package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.schema.movement.v1.MessageType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.uvms.reporting.service.serializer.MovementDtoSerializer;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@JsonSerialize(using = MovementDtoSerializer.class)
public class MovementDto {

    private String id;
    private String connectId;
    private MovementPoint position;
    private Date positionTime;
    private String status;
    private BigDecimal measuredSpeed;
    private BigDecimal calculatedSpeed;
    private int course;
    private MessageType messageType;
    private AssetDto asset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public MovementPoint getPosition() {
        return position;
    }

    public void setPosition(MovementPoint position) {
        this.position = position;
    }

    public Date getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(Date positionTime) {
        this.positionTime = positionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMeasuredSpeed() {
        return measuredSpeed;
    }

    public void setMeasuredSpeed(BigDecimal measuredSpeed) {
        this.measuredSpeed = measuredSpeed;
    }

    public BigDecimal getCalculatedSpeed() {
        return calculatedSpeed;
    }

    public void setCalculatedSpeed(BigDecimal calculatedSpeed) {
        this.calculatedSpeed = calculatedSpeed;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public AssetDto getAsset() {
        return asset;
    }

    public void setAsset(AssetDto asset) {
        this.asset = asset;
    }
}

