package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"value", "selector"})
public class PositionSelectorDTO {

    public final static String X_VALUE = "xValue";
    public final static String POSITION_TYPE_SELECTOR = "positionTypeSelector";

    private Float value;

    private Selector selector;

    private Position position;

    @Builder(builderMethodName = "PositionSelectorDTOBuilder")
    public PositionSelectorDTO(Float value, Selector selector, Position position) {
        this.value = value;
        this.selector = selector;
        this.position = position;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
