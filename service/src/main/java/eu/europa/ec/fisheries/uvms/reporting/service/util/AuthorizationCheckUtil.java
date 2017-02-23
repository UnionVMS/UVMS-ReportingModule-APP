/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.util;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
            case PRIVATE: requiredFeature = report.getCreatedBy().equals(username)?null:ReportFeatureEnum.MANAGE_ALL_REPORTS;
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
           requiredFeature = ReportFeatureEnum.MANAGE_ALL_REPORTS;
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
            requiredFeature = ReportFeatureEnum.MANAGE_ALL_REPORTS;
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

    public static ReportFeatureEnum getRequiredFeatureToListReports() {
        return ReportFeatureEnum.LIST_REPORTS;
    }

    public static List<VisibilityEnum> listAllowedVisibilityOptions(String createdBy, String currentUser, Set<String> features) {
        List<VisibilityEnum> visibilityEnumList = new LinkedList<>();

        if (createdBy.equals(currentUser) || isAllowed(ReportFeatureEnum.MANAGE_ALL_REPORTS, features)) {
            visibilityEnumList.add(VisibilityEnum.PRIVATE);
        }

        if ((createdBy.equals(currentUser) && isAllowed(ReportFeatureEnum.SHARE_REPORT_SCOPE, features)) || isAllowed(ReportFeatureEnum.MANAGE_ALL_REPORTS, features)) {
            visibilityEnumList.add(VisibilityEnum.SCOPE);
        }

        if ((createdBy.equals(currentUser) && isAllowed(ReportFeatureEnum.SHARE_REPORT_PUBLIC, features)) || isAllowed(ReportFeatureEnum.MANAGE_ALL_REPORTS, features)) {
            visibilityEnumList.add(VisibilityEnum.PUBLIC);
        }

        return visibilityEnumList;
    }

    public static boolean isAllowed(final ReportFeatureEnum requiredFeature, final Set<String> grantedFeatures) {
        boolean isAllowed = false;

        if (requiredFeature == null || grantedFeatures.contains(requiredFeature.toString())) {
            isAllowed = true;
        }
        return isAllowed;
    }

    public static boolean isEditAllowed(ReportDTO reportDTO, String currentUser, Set<String> features) {
        return isAllowed(getRequiredFeatureToEditReport(reportDTO,currentUser), features);
    }

    public static boolean isDeleteAllowed(ReportDTO reportDTO, String currentUser, Set<String> features) {
        return isAllowed(getRequiredFeatureToDeleteReport(reportDTO, currentUser), features);
    }
}