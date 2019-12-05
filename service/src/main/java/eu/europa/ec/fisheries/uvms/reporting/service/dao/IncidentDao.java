package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Stateless
public class IncidentDao {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentDao.class);

    @PersistenceContext
    private EntityManager em;


    public Incident save(Incident entity) {
        this.em.persist(entity);
        LOG.info("New Incident created with ID: " + entity.getId());
        return entity;
    }

    public List<Incident> findAllAssetNotSending(IncidentTypeEnum typeEnum) {
        TypedQuery<Incident> query = em.createNamedQuery(Incident.FIND_BY_INCIDENT_TYPE, Incident.class);
        query.setParameter("incidentType", typeEnum.name());
        return query.getResultList();
    }

    public Incident findByTicketId(UUID ticketId) {
        TypedQuery<Incident> query = em.createNamedQuery(Incident.FIND_BY_TICKET_ID, Incident.class);
        query.setParameter("ticketId", ticketId);
        return query.getSingleResult();
    }

    public Incident update(Incident entity) {
        entity.setUpdateDate(Instant.now());
        return em.merge(entity);
    }
}
