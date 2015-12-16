package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import static javax.measure.unit.NonSI.KNOT;
import static javax.measure.unit.NonSI.MILE;
import static javax.measure.unit.NonSI.NAUTICAL_MILE;
import static javax.measure.unit.SI.KILOMETER;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.measure.converter.UnitConverter;

/**
 * TODO Create Test
 */
public enum LengthType {

    MI(1,"mi"), NM(2, "nm"), KM(3,"km");

    private Integer id;
    private String displayName;

    private LengthType converter;

    LengthType(int id, String displayName) {
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

            case KM:
                unitConverter = NAUTICAL_MILE.getConverterTo(KILOMETER);
                break;

            case MI:
                unitConverter = NAUTICAL_MILE.getConverterTo(MILE);
                break;

            case NM:
                unitConverter = NAUTICAL_MILE.getConverterTo(NAUTICAL_MILE);
                break;

            default:
                break;
        }

        return unitConverter;

    }
}
