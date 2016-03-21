package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.message.bean.ReportingMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.message.event.GetReportStartAndEndDateEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.reporting.message.event.ReportingMessageEvent;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.ReportingModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRQ;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by padhyad on 3/21/2016.
 */
@Stateless
@Slf4j
public class ReportEventServiceBean implements ReportEventService {

    private static int REPORTING_MESSAGE = 1700;

    @Inject
    @ReportingMessageErrorEvent
    private Event<ReportingMessageEvent> reportingErrorEvent;

    @EJB
    private ReportingMessageServiceBean messageProducer;

    @EJB
    private ReportServiceBean reportService;

    @Override
    public void getReportDates(@Observes @GetReportStartAndEndDateEvent ReportingMessageEvent event) {
        log.info("Getting Report Start and End date");
        try {
            ReportGetStartAndEndDateRQ request = event.getReportGetStartAndEndDateRQ();
            ReportGetStartAndEndDateRS response = reportService.getReportDates(request.getNow(), request.getId(), request.getUserName(), request.getScopeName());
            messageProducer.sendModuleResponseMessage(event.getMessage(), ReportingModuleResponseMapper.mapReportGetStartAndEndDateRS(response));
        } catch (Exception e) {
            sendError(event, e);
        }

    }

    private void sendError(ReportingMessageEvent event, Exception e) {
        log.error("[ Error in Reporting Module. ]", e);
        reportingErrorEvent.fire(new ReportingMessageEvent(event.getMessage(), ReportingModuleResponseMapper.createFaultMessage(REPORTING_MESSAGE, "Exception in reporting [ " + e.getMessage())));
    }
}
