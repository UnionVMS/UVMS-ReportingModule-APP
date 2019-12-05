package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.AssetNotSending;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.AssetNotSendingUpdate;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@ApplicationScoped
@Path("sse")
@RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
public class AssetNotSendingSse {

    private final static Logger LOG = LoggerFactory.getLogger(AssetNotSendingSse.class);

    private Sse sse;
    private OutboundSseEvent.Builder eventBuilder;
    private SseBroadcaster sseBroadcaster;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.eventBuilder = sse.newEventBuilder();
        this.sseBroadcaster = sse.newBroadcaster();
    }

    public void incidentCreated(@Observes(during = TransactionPhase.AFTER_SUCCESS) @AssetNotSending Incident incident) {
        try {
            broadcastEvent(incident, "Asset Not Sending Event");
        } catch (Exception e){
            LOG.error("Error while broadcasting SSE: ", e);
            throw new RuntimeException(e);
        }
    }

    public void incidentUpdated(@Observes(during = TransactionPhase.AFTER_SUCCESS) @AssetNotSendingUpdate Incident incident) {
        try {
            broadcastEvent(incident, "Asset Not Sending Update Event");
        } catch (Exception e){
            LOG.error("Error while broadcasting SSE: ", e);
            throw new RuntimeException(e);
        }
    }

    private void broadcastEvent(Incident incident, String name) {
        OutboundSseEvent sseEvent = eventBuilder
                .name(name)
                .id("" + System.currentTimeMillis())
                .mediaType(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE)
                .data(Incident.class, incident)
                //.reconnectDelay(3000) //this one is optional
                .comment("Updated Asset")
                .build();
        sseBroadcaster.broadcast(sseEvent);
    }

    @GET
    @Path("subscribe")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listen(@Context SseEventSink sseEventSink) {
        sseEventSink.send(sse.newEvent("Welcome to Asset Not Sending Event SSE notifications."));
        sseBroadcaster.register(sseEventSink);
        sseEventSink.send(sse.newEvent("You are now registered for receiving Asset not sending event."));
    }
}
