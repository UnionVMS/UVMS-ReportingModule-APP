/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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