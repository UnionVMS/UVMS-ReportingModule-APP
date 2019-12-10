package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ec.fisheries.uvms.movement.client.MovementRestClient;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentLogDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.dto.MicroMovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.EventTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.helper.IncidentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@Stateless
public class IncidentLogServiceBean {
    private static final Logger LOG = LoggerFactory.getLogger(IncidentLogServiceBean.class);

    @Inject
    private IncidentLogDao incidentLogDao;
    @Inject
    private IncidentHelper incidentHelper;

    private ObjectMapper om = new ObjectMapper();

    @PostConstruct
    public void init() {
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public List<IncidentLog> getAssetNotSendingEventChanges(long incidentId) {
        return incidentLogDao.findAllByIncidentId(incidentId);
    }

    public void createIncidentLogForStatus(String oldValue, Incident updated) {
        try {
            StatusEnum currentStatus = updated.getStatus();
            String jsonPrevious = om.writeValueAsString(oldValue);
            String jsonCurrent = om.writeValueAsString(currentStatus);
            IncidentLog log = new IncidentLog();
            log.setCreateDate(Instant.now());
            log.setIncidentId(updated.getId());
            log.setEventType(EventTypeEnum.INCIDENT_STATUS);
            log.setPreviousValue(jsonPrevious);
            log.setCurrentValue(jsonCurrent);
            log.setMessage(EventTypeEnum.INCIDENT_STATUS.getMessage());
            incidentLogDao.save(log);
        } catch (
                JsonProcessingException e) {
            LOG.error("Error when creating MicroMovementExtended JSON object: " + e.getMessage(), e);
        }

    }

    public void createIncidentLogForManualPosition(Incident persisted, MicroMovement manual, MicroMovement latest) {
        MicroMovementDto manualPositionDto = incidentHelper.mapToMicroMovementDto(manual);
        MicroMovementDto latestPositionDto = incidentHelper.mapToMicroMovementDto(latest);

        try {
            String jsonPrevious = om.writeValueAsString(latestPositionDto);
            String jsonCurrent = om.writeValueAsString(manualPositionDto);
            IncidentLog log = new IncidentLog();
            log.setCreateDate(Instant.now());
            log.setIncidentId(persisted.getId());
            log.setEventType(EventTypeEnum.MANUEL_POSITION);
            log.setPreviousValue(jsonPrevious);
            log.setCurrentValue(jsonCurrent);
            log.setMessage(EventTypeEnum.MANUEL_POSITION.getMessage());
            incidentLogDao.save(log);
        } catch (
                JsonProcessingException e) {
            LOG.error("Error when creating MicroMovementExtended JSON object: " + e.getMessage(), e);
        }
    }
}
