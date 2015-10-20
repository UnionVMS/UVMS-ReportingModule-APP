package eu.europa.ec.fisheries.uvms.reporting.service.mock;

import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;

import java.math.BigDecimal;

public class MockSegmentData {

    static WKTWriter writer = new WKTWriter();
    static int counter = 0;

    public static MovementSegment getDto(String wkt) {
        MovementSegment dto = new MovementSegment();
        dto.setWkt(wkt);
        dto.setCourseOverGround((double) MockingUtils.randInt(1, 30));
        dto.setDistance((double) MockingUtils.randInt(1, 30));
        dto.setDuration((double) MockingUtils.randInt(1, 30));
        dto.setSpeedOverGround((double) MockingUtils.randInt(1, 30));
        dto.setCategory(SegmentCategoryType.OTHER);
        dto.setId(String.valueOf(++counter));
        return dto;
    }

}
