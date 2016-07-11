/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.model.mappper;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportingFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * Created by padhyad on 3/18/2016.
 */
public class ReportingModuleResponseMapper {

    final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    private static void validateResponse(TextMessage response, String correlationId) throws ReportingModelException {

        try {
            if (response == null) {
                throw new ReportingModelException("Error when validating response in ResponseMapper: Response is Null");
            }

            if (response.getJMSCorrelationID() == null) {
                throw new ReportingModelException("No correlationId in response (Null) . Expected was: " + correlationId);
            }

            if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
                throw new ReportingModelException("Wrong correlationId in response. Expected was: " + correlationId + " But actual was: " + response.getJMSCorrelationID());
            }

            //the following code is catching the exception in purpose. DO NOT MODIFY it!
            try{
                ReportingFault fault = JAXBMarshaller.unmarshall(response, ReportingFault.class);
                throw new ReportingModelException(fault.getCode() + " : " + fault.getFault());
            } catch (ReportingModelException e) {
                LOG.info("Expected Exception"); // Exception received in case if the validation is success
            }

        } catch (JMSException e) {
            LOG.error("JMS exception during validation ", e);
            throw new ReportingModelException("JMS exception during validation " + e.getMessage());
        }
    }

    public static ReportGetStartAndEndDateRS mapToReportGetStartAndEndDateRS(TextMessage response, String correlationId) throws ReportingModelException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, ReportGetStartAndEndDateRS.class);
        } catch (ReportingModelException e) {
            throw new ReportingModelException(e);
        }
    }

    public static String mapReportGetStartAndEndDateRS(ReportGetStartAndEndDateRS response) throws ReportingModelException {
        return JAXBMarshaller.marshall(response);
    }

    public static ReportingFault createFaultMessage(int code, String message) {
        ReportingFault fault = new ReportingFault();
        fault.setCode(code);
        fault.setFault(message);
        return fault;
    }
}