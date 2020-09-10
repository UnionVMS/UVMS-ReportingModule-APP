package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

public interface ActivityReportService {
    void createActivitiesAndTrips(FLUXFAReportMessage activityData);
}
