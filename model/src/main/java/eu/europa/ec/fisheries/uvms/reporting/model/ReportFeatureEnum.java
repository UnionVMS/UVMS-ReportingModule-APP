package eu.europa.ec.fisheries.uvms.reporting.model;

public enum ReportFeatureEnum {

    SHARE_REPORT_SCOPE("SHARE_REPORT_SCOPE"),
    SHARE_REPORT_PUBLIC("SHARE_REPORT_PUBLIC"),
    UNSHARE_FOREIGN_REPORT("UNSHARE_FOREIGN_REPORT"),
    CREATE_REPORT("CREATE_REPORT"),
    MODIFY_SCOPE_REPORT("MODIFY_SCOPE_REPORT"),
    MODIFY_PUBLIC_REPORT("MODIFY_PUBLIC_REPORT"),
    DELETE_SCOPE_REPORT("DELETE_SCOPE_REPORT"),
    DELETE_PUBLIC_REPORT("DELETE_PUBLIC_REPORT");

    String value;

    ReportFeatureEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
