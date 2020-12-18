package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm;

public interface AlarmDao {

    <T> T createEntity(T entity);

    <T> T update(T entity);

    Alarm findByGuid(String guid);
}
