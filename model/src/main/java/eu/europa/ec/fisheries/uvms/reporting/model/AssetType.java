package eu.europa.ec.fisheries.uvms.reporting.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetType {

    ASSET("asset"), GROUP("group");

    private String name;

    private AssetType(final String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
