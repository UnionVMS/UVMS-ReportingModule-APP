package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentLogDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.EventTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.IncidentLogEvent;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Stateless
public class IncidentLogServiceBean {

    @Inject
    private IncidentLogDao incidentLogDao;

    public List<IncidentLog> getAssetNotSendingEventChanges(UUID eventId) {
        return incidentLogDao.findAllByIncidentId(eventId);
    }

    public void createAssetNotSendingIncident(@Observes @IncidentLogEvent Incident incident) {
        IncidentLog log = new IncidentLog();
        log.setCreateDate(Instant.now());
        log.setIncidentId(incident.getId());
        log.setEventType(EventTypeEnum.ASSET_NOT_SENDING);
        log.setMessage("New Asset Not Sending event created");
        incidentLogDao.save(log);
    }
}
