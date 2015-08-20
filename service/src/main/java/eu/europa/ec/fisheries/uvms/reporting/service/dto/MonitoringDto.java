package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import org.geojson.FeatureCollection;

public class MonitoringDto {

    private FeatureCollection movements;

    private FeatureCollection segments;

    public FeatureCollection getMovements() {
        return movements;
    }

    public void setMovements(FeatureCollection movements) {
        this.movements = movements;
    }

    public FeatureCollection getSegments() {
        return segments;
    }

    public void setSegments(FeatureCollection segments) {
        this.segments = segments;
    }
}
