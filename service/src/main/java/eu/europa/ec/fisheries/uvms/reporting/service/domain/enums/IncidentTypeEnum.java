package eu.europa.ec.fisheries.uvms.reporting.service.domain.enums;

public enum IncidentTypeEnum {

    CALLED_NO_RESPONSE("CALLED_NO_RESPONSE"),
    ASSET_NOT_SENDING("ASSET_NOT_SENDING"),
    ASSET_SENDING_AGAIN("ASSET_SENDING_AGAIN"),
    MANUEL_POSITION("MANUEL_POSITION"),
    SYSTEM("SYSTEM");

    private String message;

    IncidentTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

