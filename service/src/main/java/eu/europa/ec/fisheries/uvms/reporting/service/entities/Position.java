package eu.europa.ec.fisheries.uvms.reporting.service.entities;

public enum Position {

    positions("positions"),
    hours("hours");

    private String name;

    Position(String value) {
        this.name = value;
    }

    public static Position getByName(String name) {
        for (Position e : Position.values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;// not found
    }

    public String getName() {
        return name;
    }

}
