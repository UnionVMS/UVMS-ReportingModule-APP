package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

public interface IncomingActivityDataService {
    void handle(String message) throws ReportingServiceException;
}
