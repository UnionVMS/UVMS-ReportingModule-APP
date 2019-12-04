package eu.europa.ec.fisheries.uvms.reporting.service.domain.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "incident")
@NamedQueries({
        @NamedQuery(name = Incident.FIND_BY_INCIDENT_TYPE, query = "SELECT i FROM Incident i WHERE i.incidentType = :incidentType"),
        @NamedQuery(name = Incident.FIND_BY_TICKET_ID, query = "SELECT i FROM Incident i WHERE i.ticketId = :ticketId")
})
public class Incident {

    public static final String FIND_BY_INCIDENT_TYPE = "Incident.findByIncidentType";
    public static final String FIND_BY_TICKET_ID = "Incident.findByTicketId";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @NotNull
    @Column(name = "asset_id")
    private UUID assetId;

    @NotNull
    @Column(name = "mobterm_id")
    private UUID mobileTerminalId;

    @NotNull
    @Column(name = "ticket_id")
    private UUID ticketId;

    @NotNull
    @Column(name = "asset_name")
    private String assetName;

    @NotNull
    @Column(name = "ircs")
    private String ircs;

    @NotNull
    @Column(name = "longitude")
    private double longitude;

    @NotNull
    @Column(name = "latitude")
    private double latitude;

    @NotNull
    @Column(name = "altitude")
    private double altitude = 0;

    @NotNull
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;

    @NotNull
    @Column(name = "incident_type")
    @Enumerated(value = EnumType.STRING)
    private IncidentTypeEnum incidentType;

    @NotNull
    @Column(name = "create_date")
    private Instant createDate;

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

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public IncidentTypeEnum getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentTypeEnum incidentType) {
        this.incidentType = incidentType;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident that = (Incident) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(assetId, that.assetId) &&
                Objects.equals(mobileTerminalId, that.mobileTerminalId) &&
                Objects.equals(ticketId, that.ticketId) &&
                Objects.equals(assetName, that.assetName) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(status, that.status) &&
                Objects.equals(incidentType, that.incidentType) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(altitude, that.altitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
