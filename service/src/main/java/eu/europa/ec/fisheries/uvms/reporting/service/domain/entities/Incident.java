package eu.europa.ec.fisheries.uvms.reporting.service.domain.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "incident")
@NamedQueries({
        @NamedQuery(name = Incident.FIND_BY_STATUS, query = "SELECT i FROM Incident i WHERE i.status = :status"),
        @NamedQuery(name = Incident.FIND_BY_TICKET_ID, query = "SELECT i FROM Incident i WHERE i.ticketId = :ticketId")
})
public class Incident {

    public static final String FIND_BY_STATUS = "Incident.findByStatus";
    public static final String FIND_BY_TICKET_ID = "Incident.findByTicketId";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "asset_id")
    private UUID assetId;

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

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "altitude")
    private double altitude;

    @NotNull
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;

    @NotNull
    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
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
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(updateDate, that.updateDate) &&
                Objects.equals(altitude, that.altitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
