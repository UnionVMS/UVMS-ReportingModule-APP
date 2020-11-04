package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ForwardReportToSubscriptionRequest;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

public interface ActivityReportService {
    void createActivitiesAndTrips(FLUXFAReportMessage activityData);

    void createActivitiesAndTrips(ForwardReportToSubscriptionRequest activityData);

}
