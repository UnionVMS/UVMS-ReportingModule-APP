package eu.europa.ec.fisheries.uvms.reporting.service.domain.dto;

public enum MovementSourceType {
    INMARSAT_C,
    AIS,
    IRIDIUM,
    MANUAL,
    OTHER,
    NAF;

    MovementSourceType() {
    }

    public String value() {
        return this.name();
    }

    public static MovementSourceType fromValue(String v) {
        return valueOf(v);
    }
}