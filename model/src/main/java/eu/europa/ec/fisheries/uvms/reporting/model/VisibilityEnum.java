package eu.europa.ec.fisheries.uvms.reporting.model;

public enum VisibilityEnum {
	
	PRIVATE("private"),
	SCOPE("scope"),
	PUBLIC("public");

    private String name;

    VisibilityEnum(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }
}
