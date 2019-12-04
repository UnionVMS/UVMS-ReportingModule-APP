package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.movement.client.MovementRestClient;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.AssetNotSending;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.AssetNotSendingUpdate;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.dto.TicketDto;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.helper.IncidentHelper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class IncidentServiceBean {

    @Inject
    @AssetNotSendingUpdate
    private Event<Incident> assetNotSendingEvent;

    @Inject
    private IncidentHelper incidentHelper;

    @EJB
    private MovementRestClient movementClient;

    @Inject
    private IncidentDao incidentDao;

    public List<Incident> getAssetNotSendingList() {
        List<Incident> assetNotSendingList = incidentDao.findAllAssetNotSending(IncidentTypeEnum.ASSET_NOT_SENDING);
        if(assetNotSendingList == null) return new ArrayList<>();
        return assetNotSendingList;
    }

    public void createAssetNotSendingIncident(@Observes @AssetNotSending TicketDto ticket) {
        MicroMovement movement = movementClient.getMicroMovementById(UUID.fromString(ticket.getMovementId()));
        Incident incident = incidentHelper.constructIncident(ticket, movement);
        Incident created = incidentDao.save(incident);

        assetNotSendingEvent.fire(created);
    }
}