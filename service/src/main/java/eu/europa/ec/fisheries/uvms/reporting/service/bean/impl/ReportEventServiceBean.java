/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportEventService;
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

@Stateless
@Slf4j
public class ReportEventServiceBean implements ReportEventService {

    private static final int REPORTING_MESSAGE = 1700;

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