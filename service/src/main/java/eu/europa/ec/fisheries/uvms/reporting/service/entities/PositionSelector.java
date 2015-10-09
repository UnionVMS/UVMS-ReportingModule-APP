package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.Builder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class PositionSelector {

    private int value;

    @Enumerated(EnumType.STRING)
    private Selector selector;

    @Enumerated(EnumType.STRING)
    private Position position;

    PositionSelector(){

    }

    @Builder(builderMethodName = "PositionSelectorBuilder")
    public PositionSelector(int value, Selector selector, Position position) {
        this.value = value;
        this.selector = selector;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Selector getSelector() {

        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
