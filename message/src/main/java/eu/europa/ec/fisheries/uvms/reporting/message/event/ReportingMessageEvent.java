package eu.europa.ec.fisheries.uvms.reporting.message.event;

import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRQ;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportingFault;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;

import javax.jms.TextMessage;

/**
 * Created by padhyad on 3/21/2016.
 */
public class ReportingMessageEvent {

    private TextMessage message;
    private ReportGetStartAndEndDateRQ reportGetStartAndEndDateRQ;
    private ReportingFault fault;

    public ReportingMessageEvent(TextMessage message, ReportGetStartAndEndDateRQ reportGetStartAndEndDateRQ) {
        this.message = message;
        this.reportGetStartAndEndDateRQ = reportGetStartAndEndDateRQ;
    }

    public ReportingMessageEvent(TextMessage message, ReportingFault fault) {
        this.message = message;
        this.fault = fault;
    }

    public TextMessage getMessage() {
        return message;
    }

    public ReportingFault getFault() {
        return fault;
    }

    public ReportGetStartAndEndDateRQ getReportGetStartAndEndDateRQ() {
        return reportGetStartAndEndDateRQ;
    }
}
