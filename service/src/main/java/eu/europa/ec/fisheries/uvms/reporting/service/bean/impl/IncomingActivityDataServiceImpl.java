/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncomingActivityDataService;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

@ApplicationScoped
@Slf4j
public class IncomingActivityDataServiceImpl implements IncomingActivityDataService {

    @Inject
    private ActivityReportService activityReportService;

    @Override
    public void handle(String message) throws ReportingServiceException {
        FLUXFAReportMessage activityData = unmarshal(message);

        activityReportService.createActivitiesAndTrips(activityData);
    }

    private FLUXFAReportMessage unmarshal(String message) throws ReportingServiceException {
        try {
            return JAXBUtils.unMarshallMessage(message, FLUXFAReportMessage.class);
        } catch (JAXBException e) {
            throw new ReportingServiceException("Cannot parse message");
        }
    }
}