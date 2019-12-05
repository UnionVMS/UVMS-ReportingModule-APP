package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.movement.client.MovementRestClient;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.dto.TicketDto;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.helper.IncidentHelper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
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
    private IncidentDao incidentDao;

    public List<Incident> getAssetNotSendingList() {
        List<Incident> assetNotSendingList = incidentDao.findAllAssetNotSending(IncidentTypeEnum.ASSET_NOT_SENDING);
        return assetNotSendingList.stream()
                .filter(isOpenOrClosedIn12Hours).collect(Collectors.toList());
    }

    private Predicate<Incident> isOpenOrClosedIn12Hours = (incident -> incident.getUpdateDate() == null ||
            (incident.getUpdateDate() != null && incident.getUpdateDate()
                            .minus(12, ChronoUnit.HOURS)
                            .isAfter(incident.getCreateDate())));

    public void createAssetNotSendingIncident(TicketDto ticket) {
        MicroMovement movement = movementClient.getMicroMovementById(UUID.fromString(ticket.getMovementId()));
        Incident incident = incidentHelper.constructIncident(ticket, movement);
        Incident created = incidentDao.save(incident);

        incidentLogServiceBean.createAssetIncidentLog(created, IncidentTypeEnum.ASSET_NOT_SENDING);
    }

    public void updateAssetNotSendingIncident(TicketDto ticket) {
        Incident entity = incidentDao.findByTicketId(UUID.fromString(ticket.getTicketId()));
        entity.setStatus(StatusEnum.valueOf(ticket.getStatus()));
        Incident updated = incidentDao.update(entity);

        incidentLogServiceBean.createAssetIncidentLog(updated, IncidentTypeEnum.ASSET_NOT_SENDING);
    }
}