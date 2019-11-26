package eu.europa.ec.fisheries.uvms.reporting.service.domain;

import eu.europa.ec.fisheries.uvms.movement.service.dto.MicroMovement;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ans_event")
public class AssetNotSendingEvent {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "asset_id")
    private UUID assetId;

    @Column(name = "mobileterminal_id")
    private UUID mobileTerminalId;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "ircs")
    private String ircs;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "altitude")
    private double altitude;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;

    @Transient
    private MicroMovement microMovement;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID();
        setLatLong();
    }

    @PreUpdate
    public void preUpdate() {
        setLatLong();
    }

    private void setLatLong() {
        if (microMovement != null && microMovement.getLocation() != null) {
            if (microMovement.getLocation().getLongitude() != null) {
                this.longitude = microMovement.getLocation().getLongitude();
            }
            if (microMovement.getLocation().getLatitude() != null) {
                this.latitude = microMovement.getLocation().getLatitude();
            }
            if (microMovement.getLocation().getAltitude() != null) {
                this.altitude = microMovement.getLocation().getLatitude();
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAssetId() {
        return assetId;
    }

    public void setAssetId(UUID assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getIrcs() {
        return ircs;
    }

    public void setIrcs(String ircs) {
        this.ircs = ircs;
    }

    public MicroMovement getMicroMovement() {
        return microMovement;
    }

    public void setMicroMovement(MicroMovement microMovement) {
        this.microMovement = microMovement;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public UUID getMobileTerminalId() {
        return mobileTerminalId;
    }

    public void setMobileTerminalId(UUID mobileTerminalId) {
        this.mobileTerminalId = mobileTerminalId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
