package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;

public interface ActivityRepository {

    Activity createActivityEntity(Activity entity);

    Trip createTripEntity(Trip trip);
}
