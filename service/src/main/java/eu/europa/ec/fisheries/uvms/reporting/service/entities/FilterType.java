package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterType {

    VESSEL("vessel"),
    VGROUP("vgroup"),
    COMMON("common"),
    VMSPOS("vmspos"),
    VMSTRACK("vmstrack");

    private String name;

    FilterType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
