package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * //TODO create test
 */
@Embeddable
public class PositionSelector {

    private Long value;

    @Enumerated(EnumType.STRING)
    private Selector selector;

    @Enumerated(EnumType.STRING)
    private Position position;

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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
