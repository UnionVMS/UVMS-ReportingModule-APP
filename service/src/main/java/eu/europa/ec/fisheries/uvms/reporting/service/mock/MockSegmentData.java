package eu.europa.ec.fisheries.uvms.reporting.service.mock;

import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategory;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;

import java.math.BigDecimal;

public class MockSegmentData {

    static WKTWriter writer = new WKTWriter();
    static int counter = 0;

    public static MovementSegment getDto(String wkt) {
        MovementSegment dto = new MovementSegment();
        dto.setWkt(wkt);
        dto.setCourseOverGround(BigDecimal.valueOf(MockingUtils.randInt(1, 30)));
        dto.setDistance(BigDecimal.valueOf(MockingUtils.randInt(1, 30)));
        dto.setDuration(BigDecimal.valueOf(MockingUtils.randInt(1, 30)));
        dto.setSpeedOverGround(BigDecimal.valueOf((double) MockingUtils.randInt(1, 30)));
        dto.setCategory(SegmentCategory.TRACK);
        dto.setId(String.valueOf(++counter));
        return dto;
    }

}
