package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Selector {
    ALL("ALL"), LAST("LAST");

    private String name;

    private Selector(final String name){
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
