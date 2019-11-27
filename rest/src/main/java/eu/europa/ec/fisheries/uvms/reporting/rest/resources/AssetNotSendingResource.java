/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetNotSendingEventBean;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("assetNotSendingEvents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssetNotSendingResource {

    @Inject
    private AssetNotSendingEventBean assetNotSendingEventBean;

    @GET
    public Response getAssetNotSendingEvents() {
        List<Incident> notSendingList = assetNotSendingEventBean.getAssetNotSendingList();
        return Response.ok(notSendingList).build();
    }

    @GET
    @Path("assetNotSendingEventChanges/{eventId}")
    public Response getAssetNotSendingEventChanges(@PathParam("eventId") UUID eventId) {
        List<IncidentLog> eventChanges = assetNotSendingEventBean.getAssetNotSendingEventChanges(eventId);
        return Response.ok(eventChanges).build();
    }
}