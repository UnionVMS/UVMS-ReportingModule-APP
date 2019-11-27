package eu.europa.ec.fisheries.uvms.reporting.service.domain.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.EventEnum;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "incident_log")
public class IncidentLog {

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
    private EventEnum eventType;

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

    public EventEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventEnum eventType) {
        this.eventType = eventType;
    }
}
