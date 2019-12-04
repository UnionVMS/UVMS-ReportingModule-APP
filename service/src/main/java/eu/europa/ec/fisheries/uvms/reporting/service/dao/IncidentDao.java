package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.IncidentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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

}
