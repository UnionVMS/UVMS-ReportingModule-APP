package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ForwardReportToSubscriptionRequest;

public interface ActivityReportService {

    void processReports(ForwardReportToSubscriptionRequest activityData);

}
