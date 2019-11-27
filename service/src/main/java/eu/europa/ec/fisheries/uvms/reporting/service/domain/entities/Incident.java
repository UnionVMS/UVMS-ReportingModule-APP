package eu.europa.ec.fisheries.uvms.reporting.service.domain.entities;

import eu.europa.ec.fisheries.uvms.movement.service.dto.MicroMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @Column(name = "asset_id")
    private UUID assetId;

    @Column(name = "mobterm_id")
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

    @Column(name = "incident_type")
    @Enumerated(value = EnumType.STRING)
    private IncidentEnum incidentType;

    @Transient
    private MicroMovement microMovement;

    @PrePersist
    @PreUpdate
    public void preUpsert() {
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

    public IncidentEnum getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentEnum incidentType) {
        this.incidentType = incidentType;
    }
}
