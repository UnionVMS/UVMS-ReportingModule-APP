package eu.europa.ec.fisheries.uvms.reporting.service.entities;

public enum Selector {

    ALL("all"),
    LAST("last");

    private String name;

    private Selector(final String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
