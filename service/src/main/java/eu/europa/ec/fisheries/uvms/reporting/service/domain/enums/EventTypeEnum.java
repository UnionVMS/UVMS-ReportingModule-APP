package eu.europa.ec.fisheries.uvms.reporting.service.domain.enums;

public enum EventTypeEnum {

    CALLED_NO_RESPONSE("Båt har blivit uppringt, skäppare svarar inte"),
    ASSET_NOT_SENDING(""),
    ASSET_SENDING_AGAIN("");

    private String message;

    private EventTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

