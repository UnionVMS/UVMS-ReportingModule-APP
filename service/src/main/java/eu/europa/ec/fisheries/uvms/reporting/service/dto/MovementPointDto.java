package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.reporting.service.serializer.MovementPointDtoSerializer;

/**
 * //TODO create test
 */
@JsonSerialize(using = MovementPointDtoSerializer.class)
public class MovementPointDto {

    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
