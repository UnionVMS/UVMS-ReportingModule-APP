package eu.europa.ec.fisheries.uvms.reporting.service.domain.dto;

import java.io.Serializable;

public class TicketDto implements Serializable {

    private String ticketId;
    private String assetId;
    private String mobTermId;
    private String movementId;
    private String status;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getMobTermId() {
        return mobTermId;
    }

    public void setMobTermId(String mobTermId) {
        this.mobTermId = mobTermId;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "ticketId='" + ticketId + '\'' +
                ", assetId='" + assetId + '\'' +
                ", mobTermId='" + mobTermId + '\'' +
                ", movementId='" + movementId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
