package eu.europa.ec.fisheries.uvms.reporting.service.entities;

public enum Position {

    POSITION("position"),
    HOURS("hours");

    private String name;

    private Position(final String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
