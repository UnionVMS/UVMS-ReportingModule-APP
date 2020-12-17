package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;

public interface AlarmReportService {

    void createOrUpdate(TicketType alarm);
}
