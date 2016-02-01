package eu.europa.ec.fisheries.uvms.reporting.model;

public class Asset {

    private String guid;
    private String name;
    private AssetType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetType getType() {
        return type;
    }

    public void setAssetType(AssetType type) {
        this.type = type;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
