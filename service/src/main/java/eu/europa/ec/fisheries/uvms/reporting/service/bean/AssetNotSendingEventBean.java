package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.AssetNotSendingAudit;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.AssetNotSendingEvent;
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
    private Event<AssetNotSendingEvent> assetNotSendingEventEvent;


    public List<AssetNotSendingEvent> getAssetNotSendingList() {
        // todo: get all assets not sending

        // todo: get micro movements

        // todo: map everything into AssetNotSendingEvent object

        return new ArrayList<>();
    }

    public List<AssetNotSendingAudit> getAssetNotSendingEventChanges(UUID eventId) {
        // todo: get change audit list for a given event id
        return new ArrayList<>();
    }
}
