package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.movement.client.MovementRestClient;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.dto.StatusDto;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.EventTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.IncidentCreate;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.IncidentUpdate;
import eu.europa.ec.fisheries.uvms.reporting.service.helper.IncidentHelper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Stateless
public class IncidentServiceBean {

    @Inject
    private IncidentLogServiceBean incidentLogServiceBean;

    @Inject
    private IncidentHelper incidentHelper;

    @EJB
    private MovementRestClient movementClient;

    @Inject
    @IncidentCreate
    private Event<Incident> createdIncident;

    @Inject
    @IncidentUpdate
    private Event<Incident> updatedIncident;

    @Inject
    private IncidentDao incidentDao;

    public List<Incident> getAssetNotSendingList() {
        List<Incident> assetNotSendingList = incidentDao.findAllAssetNotSending(StatusEnum.ASSET_NOT_SENDING);
        return assetNotSendingList.stream()
                .filter(isOpenOrClosedIn12Hours).collect(Collectors.toList());
    }

    private Predicate<Incident> isOpenOrClosedIn12Hours = (incident -> incident.getUpdateDate() == null ||
            (incident.getUpdateDate() != null && incident.getUpdateDate()
                            .minus(12, ChronoUnit.HOURS)
                            .isAfter(incident.getCreateDate())));

    public void createIncident(TicketType ticket) {
        MicroMovement movement = movementClient.getMicroMovementById(UUID.fromString(ticket.getMovementGuid()));
        Incident incident = incidentHelper.constructIncident(ticket, movement);
        incidentDao.save(incident);
        createdIncident.fire(incident);
    }

    public void updateIncident(TicketType ticket) {
        Incident persisted = incidentDao.findByTicketId(UUID.fromString(ticket.getGuid()));
        // MicroMovement microMovement = movementClient.getMicroMovementById(UUID.fromString(ticket.getMovementGuid()));

        String ruleName = ticket.getRuleName();

        if ("Asset not sending".equals(ruleName)) {
            incidentHelper.updateAssetNotSendingStatus(ticket, persisted);
            Incident updated = incidentDao.update(persisted);
            updatedIncident.fire(updated);
            incidentLogServiceBean.createIncidentLogForStatus(persisted, updated, EventTypeEnum.INCIDENT_STATUS);
        }
    }

    public Incident updateIncidentStatus(long incidentId, StatusDto statusDto) throws Exception {
        Incident persisted = incidentDao.findById(incidentId);
        persisted.setStatus(StatusEnum.valueOf(statusDto.getStatus()));
        Incident updated = incidentDao.update(persisted);
        incidentLogServiceBean.createIncidentLogForStatus(persisted, updated, EventTypeEnum.INCIDENT_STATUS);
        return updated;
    }
}