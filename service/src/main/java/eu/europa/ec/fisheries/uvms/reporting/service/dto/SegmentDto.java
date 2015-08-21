package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.LineString;

import java.math.BigDecimal;

public class SegmentDto {

    private LineString geometry;
    private BigDecimal averageSpeed;
    private BigDecimal averageCourse;

    public LineString getGeometry() {
        return geometry;
    }

    public void setGeometry(LineString geometry) {
        this.geometry = geometry;
    }

    public BigDecimal getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(BigDecimal averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public BigDecimal getAverageCourse() {
        return averageCourse;
    }

    public void setAverageCourse(BigDecimal averageCourse) {
        this.averageCourse = averageCourse;
    }
}
