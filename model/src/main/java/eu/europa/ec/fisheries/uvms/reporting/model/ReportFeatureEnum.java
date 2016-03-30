package eu.europa.ec.fisheries.uvms.reporting.model;

public enum ReportFeatureEnum {

    LIST_REPORTS("LIST_REPORTS"),
    SHARE_REPORT_SCOPE("SHARE_REPORT_SCOPE"),
    SHARE_REPORT_PUBLIC("SHARE_REPORT_PUBLIC"),
    CREATE_REPORT("CREATE_REPORT"),
    MANAGE_ALL_REPORTS("MANAGE_ALL_REPORTS");

    String value;

    ReportFeatureEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
