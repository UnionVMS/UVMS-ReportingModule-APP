package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.reporting.service.serializer.MovementPointDtoSerializer;
import eu.europa.ec.fisheries.uvms.reporting.service.serializer.SegmentDtoSerializer;

import java.math.BigDecimal;

/**
 * //TODO create test
 */
@JsonSerialize(using = SegmentDtoSerializer.class)
public class SegmentDto {

    private Geometry presentPosition;
    private Geometry previousPosition;

    private BigDecimal averageSpeed;
    private BigDecimal averageCourse;

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

    public Geometry getPresentPosition() {
        return presentPosition;
    }

    public void setPresentPosition(Geometry presentPosition) {
        this.presentPosition = presentPosition;
    }

    public Geometry getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Geometry previousPosition) {
        this.previousPosition = previousPosition;
    }
}
