package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Catch;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;

public interface ActivityRepository {

    Activity createActivityEntity(Activity entity);

    Catch createCatchEntity(Catch speciesCatch);

    Trip createTripEntity(Trip entity);

    Area createArea(Area entity);

    Area findAreaByTypeCodeAndAreaCode(String areaTypeCode, String areaCode);
}
