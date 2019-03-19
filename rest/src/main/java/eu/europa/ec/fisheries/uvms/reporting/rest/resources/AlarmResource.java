/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.reporting.message.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.impl.AlarmServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.rules.AlarmMovementList;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/alarms")
@Slf4j
public class AlarmResource extends UnionVMSResource {

    @EJB
    private AlarmServiceBean alarmService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAlarms(AlarmMovementList alarmMovementList, @Context HttpServletRequest request) {
        try {
            return createSuccessResponse(alarmService.getAlarmsForMovements(alarmMovementList, request.getRemoteUser()));
        } catch (ReportingServiceException e) {
            log.error("Unable to get alarms.", e);
            return createErrorResponse(e.getMessage());
        }
    }
}