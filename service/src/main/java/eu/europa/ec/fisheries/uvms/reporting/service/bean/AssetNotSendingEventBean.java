package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.VesselNotSendingEvent;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class AssetNotSendingEventBean {

    @Inject
    @VesselNotSendingEvent
    private Event<Incident> assetNotSendingEventEvent;


    public List<Incident> getAssetNotSendingList() {
        // todo: get all assets not sending

        // todo: get micro movements

        // todo: map everything into AssetNotSendingEvent object

        return new ArrayList<>();
    }

    public List<IncidentLog> getAssetNotSendingEventChanges(UUID eventId) {
        // todo: get change audit list for a given event id
        return new ArrayList<>();
    }
}
