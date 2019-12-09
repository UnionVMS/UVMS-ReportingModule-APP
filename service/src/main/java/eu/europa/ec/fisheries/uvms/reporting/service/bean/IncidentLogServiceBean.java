package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentLogDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.EventTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Stateless
public class IncidentLogServiceBean {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentLogServiceBean.class);

    @Inject
    private IncidentLogDao incidentLogDao;

    private ObjectMapper om = new ObjectMapper();

    @PostConstruct
    public void init() {
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public List<IncidentLog> getAssetNotSendingEventChanges(long incidentId) {
        return incidentLogDao.findAllByIncidentId(incidentId);
    }

    public void createIncidentLogForStatus(Incident persisted, Incident updated, EventTypeEnum type) {
        try {

            switch (type) {
                case MANUEL_POSITION:
                    // Todo: Implement this.
                    break;
                case INCIDENT_STATUS:
                    StatusEnum preStatut = persisted.getStatus();
                    StatusEnum curStatus = updated.getStatus();
                    String jsonPreStatus = om.writeValueAsString(preStatut);
                    String jsonCurStatus = om.writeValueAsString(curStatus);
                    IncidentLog statusLog = new IncidentLog();
                    statusLog.setCreateDate(Instant.now());
                    statusLog.setIncidentId(persisted.getId());
                    statusLog.setEventType(type);
                    statusLog.setPreviousValue(jsonPreStatus);
                    statusLog.setCurrentValue(jsonCurStatus);
                    statusLog.setMessage(type.getMessage());
                    incidentLogDao.save(statusLog);
                    break;
            }


        } catch (JsonProcessingException e) {
            LOG.error("Error when creating MicroMovementExtended JSON object: " + e.getMessage(), e);
        }
    }
}
