package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ActivityReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Catch;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CatchLocation;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CatchProcessing;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Location;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;

import java.util.List;

public interface ActivityRepository {

    Activity createActivityEntity(Activity entity);

    Catch createCatchEntity(Catch speciesCatch);

    Trip createTripEntity(Trip entity);

    Location createLocation(Location entity);

    Location findLocationByTypeCodeAndCode(String typeCode, String code);

    void updateOlderReportsAsNotLatest(String faReportId, Long latestActivityId);

    CatchLocation createActivityCatchLocation(CatchLocation loc);

    CatchProcessing createActivityCatchProcessing(CatchProcessing processing);

    List<ActivityReportResult> executeQuery(String query);

}
