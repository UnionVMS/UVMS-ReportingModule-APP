package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentLogDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Stateless
public class IncidentLogServiceBean {

    @Inject
    private IncidentLogDao incidentLogDao;

    public List<IncidentLog> getAssetNotSendingEventChanges(UUID incidentId) {
        return incidentLogDao.findAllByIncidentId(incidentId);
    }

    public void createAssetIncidentLog(Incident incident, IncidentTypeEnum type) {
        IncidentLog log = new IncidentLog();
        log.setCreateDate(Instant.now());
        log.setIncidentId(incident.getId());
        log.setEventType(type);
        log.setMessage(type.getMessage());
        incidentLogDao.save(log);
    }

}
