package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

public interface IncomingEventDataService {

    void handle(String message) throws ReportingServiceException;

    boolean canHandle(String eventType);
}
