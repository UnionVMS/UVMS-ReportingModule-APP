package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;

public interface ActivityDao {

    <T> T createEntity(T entity);

    <T> T update(T entity);

    <T> T findById(Long id, Class<T> clazz);

    Area findAreaByTypeCodeAndAreaCode(String typeCode, String areaCode);
}
