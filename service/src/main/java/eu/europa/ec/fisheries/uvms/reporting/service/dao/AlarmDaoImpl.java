package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class AlarmDaoImpl implements AlarmDao {

    @PersistenceContext(unitName = "reportingPUposgres")
    private EntityManager em;

    @Override
    public <T> T createEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <T> T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public Alarm findByGuid(String guid) {
        Query nativeQuery = em.createNativeQuery("SELECT * FROM reporting.alarm WHERE guid = :guid", Alarm.class);
        nativeQuery.setParameter("guid", guid);
        try {
            return (Alarm) nativeQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
