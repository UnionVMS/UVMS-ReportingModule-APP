package eu.europa.ec.fisheries.uvms.reporting.service.domain.enums;

public enum EventTypeEnum {
    MANUEL_POSITION("New Manual position created"),
    INCIDENT_STATUS("Incident Status updated");

    private String message;

    EventTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

