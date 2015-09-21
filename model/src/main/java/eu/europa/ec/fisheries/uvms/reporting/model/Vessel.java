package eu.europa.ec.fisheries.uvms.reporting.model;

public class Vessel {

    private String guid;
    private String name;
    private VesselType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VesselType getType() {
        return type;
    }

    public void setVesselType(VesselType type) {
        this.type = type;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
