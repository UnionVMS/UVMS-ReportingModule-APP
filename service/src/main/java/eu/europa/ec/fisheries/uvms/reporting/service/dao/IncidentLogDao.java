package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.IncidentLog;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Stateless
public class IncidentLogDao {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentLogDao.class);

    @PersistenceContext
    private EntityManager em;


    public void save(IncidentLog entity) {
        this.em.persist(entity);
        LOG.info("New Incident Log created with ID: " + entity.getId());
    }

    public List<IncidentLog> findAllByIncidentId(UUID eventId) {
        TypedQuery<IncidentLog> query = em.createNamedQuery(IncidentLog.FIND_ALL_BY_INCIDENT_ID, IncidentLog.class);
        query.setParameter("eventId", eventId);
        return query.getResultList();
    }
}
