package eu.europa.ec.fisheries.uvms.reporting.service.domain.enums;

public enum EventTypeEnum {
    MANUEL_POSITION("MANUEL_POSITION"),
    INCIDENT_STATUS("INCIDENT_STATUS");

    private String message;

    EventTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

