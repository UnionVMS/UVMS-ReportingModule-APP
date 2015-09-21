package eu.europa.ec.fisheries.uvms.reporting.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VesselType {

    VESSEL("vessel"), GROUP("group");

    private String name;

    private VesselType(final String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
