package eu.europa.ec.fisheries.uvms.reporting.service.domain.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "incident_log")
@NamedQueries({
        @NamedQuery(name = IncidentLog.FIND_ALL_BY_INCIDENT_ID, query = "SELECT i FROM IncidentLog i WHERE i.incidentId = :incidentId"),
})
public class IncidentLog {

    public static final String FIND_ALL_BY_INCIDENT_ID = "IncidentLog.findByIncidentId";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @NotNull
    @Column(name = "incident_id")
    private long incidentId;

    @NotNull
    @Column(name = "message")
    @Lob
    private String message;

    @NotNull
    @Column(name = "event_type")
    @Enumerated(value = EnumType.STRING)
    private IncidentTypeEnum eventType;

    @NotNull
    @Column(name = "create_date")
    private Instant createDate;

    @Lob
    @Column(name = "previous_value")
    private String previousValue;

    @Lob
    @Column(name = "current_value")
    private String currentValue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(long incidentId) {
        this.incidentId = incidentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IncidentTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(IncidentTypeEnum eventType) {
        this.eventType = eventType;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentLog that = (IncidentLog) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(incidentId, that.incidentId) &&
                Objects.equals(message, that.message) &&
                Objects.equals(previousValue, that.previousValue) &&
                Objects.equals(currentValue, that.currentValue) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
