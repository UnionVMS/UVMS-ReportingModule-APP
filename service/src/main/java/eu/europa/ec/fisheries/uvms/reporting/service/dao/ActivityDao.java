package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.ActivityReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Location;

import java.util.List;

public interface ActivityDao {

    <T> T createEntity(T entity);

    <T> T update(T entity);

    <T> T findById(Long id, Class<T> clazz);

    Location findLocationByTypeCodeAndCode(String typeCode, String code);

    int updateOlderReportsAsNotLatest(String faReportId, Long latestActivityId);

    List<ActivityReportResult> executeQuery(String query);
}
