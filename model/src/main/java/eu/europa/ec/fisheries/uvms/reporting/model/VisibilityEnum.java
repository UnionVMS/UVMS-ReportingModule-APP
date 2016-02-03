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

    public static VisibilityEnum getByName(String name) {
        for(VisibilityEnum e: VisibilityEnum.values()) {
            if(e.name == name) {
                return e;
            }
        }
        return null;// not found
    }
}
