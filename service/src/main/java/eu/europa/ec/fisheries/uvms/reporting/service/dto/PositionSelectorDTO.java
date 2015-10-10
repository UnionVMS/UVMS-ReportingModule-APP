package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import lombok.Builder;

import java.security.InvalidParameterException;

public class PositionSelectorDTO {

    private Long value;

    private Selector selector;

    private Position position;

    @Builder(builderMethodName = "PositionSelectorDTOBuilder")
    public PositionSelectorDTO(Long value, Selector selector, Position position) {
        this.value = value;
        this.selector = selector;
        this.position = position;
        validate(this);
    }

    private void validate(PositionSelectorDTO positionSelectorDTO) throws InvalidParameterException {

    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
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
