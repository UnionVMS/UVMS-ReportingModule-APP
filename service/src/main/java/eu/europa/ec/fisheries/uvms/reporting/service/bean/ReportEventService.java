package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.message.event.GetReportStartAndEndDateEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageEvent;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

/**
 * Created by padhyad on 3/21/2016.
 */
@Local
public interface ReportEventService {
    void getReportDates(@Observes @GetReportStartAndEndDateEvent ReportingMessageEvent event);
}
