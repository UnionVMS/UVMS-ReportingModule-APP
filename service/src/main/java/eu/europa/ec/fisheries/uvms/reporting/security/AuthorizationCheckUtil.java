package eu.europa.ec.fisheries.uvms.reporting.security;

import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;

import static eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum.*;

/**
 * Created by georgige on 10/1/2015.
 */

//TODO unit tests!!!
public class AuthorizationCheckUtil {

    private AuthorizationCheckUtil(){

    }

    /**
     *
     * @param username
     * @param report
     * @return null if no feature was required
     */
    public static ReportFeatureEnum getRequiredFeatureToShareReport(ReportDTO report, String username) {
        ReportFeatureEnum requiredFeature = null;
        switch (report.getVisibility()) {
            case SCOPE :
                requiredFeature = ReportFeatureEnum.SHARE_REPORT_SCOPE;
                break;
            case PUBLIC :
                requiredFeature = ReportFeatureEnum.SHARE_REPORT_PUBLIC;
                break;
            default:
                break;
        }
        return requiredFeature;
    }

    /**
     *
     * @param username
     * @param report
     * @return null if no feature was required
     */
    public static ReportFeatureEnum getRequiredFeatureToEditReport(ReportDTO report, String username) {
        ReportFeatureEnum requiredFeature = null;

        if (!report.getCreatedBy().equals(username)) {
            if (report.getVisibility() == SCOPE) {
                requiredFeature = ReportFeatureEnum.MODIFY_SCOPE_REPORT;
            } else if (report.getVisibility() == PUBLIC) {
                requiredFeature = ReportFeatureEnum.MODIFY_PUBLIC_REPORT;
            }
        }

        return requiredFeature;
    }

    /**
     *
     * @param username
     * @param report
     * @return null if no feature was required
     */
    public static ReportFeatureEnum getRequiredFeatureToDeleteReport(ReportDTO report, String username) {
        ReportFeatureEnum requiredFeature = null;

        if (!report.getCreatedBy().equals(username)) {
            if (report.getVisibility() == SCOPE) {
                requiredFeature = ReportFeatureEnum.DELETE_SCOPE_REPORT;
            } else if (report.getVisibility() == PUBLIC) {
                requiredFeature = ReportFeatureEnum.DELETE_PUBLIC_REPORT;
            }
        }

        return requiredFeature;
    }


    /**
     *
     * @param username
     * @param report
     * @return null if no feature was required
     */
    public static ReportFeatureEnum getRequiredFeatureToCreateReport(ReportDTO report, String username) {
        return ReportFeatureEnum.CREATE_REPORT;
    }
}
