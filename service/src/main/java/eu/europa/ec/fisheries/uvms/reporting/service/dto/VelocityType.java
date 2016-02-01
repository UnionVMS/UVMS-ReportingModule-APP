package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import static javax.measure.unit.NonSI.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.measure.converter.UnitConverter;

public enum VelocityType {

    KPH(1, "kph"), MPH(2, "mph"), KTS(3, "kts");

    private Integer id;
    private String displayName;

    VelocityType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonIgnore
    public UnitConverter getConverter() {

        UnitConverter unitConverter = null;

        switch (this){

            case KPH:
                unitConverter = KNOT.getConverterTo(KILOMETERS_PER_HOUR);
                break;

            case MPH:
                unitConverter = KNOT.getConverterTo(MILES_PER_HOUR);
                break;

            case KTS:
                unitConverter = KNOT.getConverterTo(KNOT);
                break;

            default:
                break;
        }

        return unitConverter;

    }
}
