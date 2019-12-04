package eu.europa.ec.fisheries.uvms.reporting.service.domain.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.EventTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "incident_log")
@NamedQueries({
        @NamedQuery(name = IncidentLog.FIND_ALL_BY_INCIDENT_ID, query = "SELECT i FROM IncidentLog i WHERE i.eventId = :eventId"),
})
public class IncidentLog {

    public static final String FIND_ALL_BY_INCIDENT_ID = "IncidentLog.findByIncidentId";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "message")
    private String message;

    @Column(name = "event_type")
    @Enumerated(value = EnumType.STRING)
    private EventTypeEnum eventType;

    @NotNull
    @Column(name = "create_date")
    private Instant createDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
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
        IncidentLog that = (IncidentLog) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(message, that.message) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
