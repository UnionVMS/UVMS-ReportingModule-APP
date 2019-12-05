package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovement;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovementExtended;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.IncidentLogDao;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
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

    public List<IncidentLog> getAssetNotSendingEventChanges(UUID incidentId) {
        return incidentLogDao.findAllByIncidentId(incidentId);
    }

    public void createAssetIncidentLog(Incident incident, IncidentTypeEnum type, MicroMovement microMovement, UUID assetId) {
//        try {
            IncidentLog log = new IncidentLog();
            log.setCreateDate(Instant.now());
            log.setIncidentId(incident.getId());
            log.setEventType(type);
            log.setMessage(type.getMessage());
//            MicroMovementExtended extended = new MicroMovementExtended();
//            extended.setMicroMove(microMovement);
//            extended.setAsset(assetId.toString());
//            String actualPosition = om.writeValueAsString(extended);
            incidentLogDao.save(log);
//        } catch (JsonProcessingException e) {
//           LOG.error("Error when creating MicroMovementExtended JSON object: " + e.getMessage(), e);
//        }
    }

}
