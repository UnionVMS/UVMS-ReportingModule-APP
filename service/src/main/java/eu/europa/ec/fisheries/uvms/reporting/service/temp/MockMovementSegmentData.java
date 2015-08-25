package eu.europa.ec.fisheries.uvms.reporting.service.temp;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;

import java.util.ArrayList;
import java.util.List;

public class MockMovementSegmentData {

    public static MovementSegment getDto(Long id) {
        MovementSegment dto = new MovementSegment();
        dto.setId(id.toString());
        dto.setPresentPosition(getMovementPoint());
        dto.setPreviousPosition(getMovementPoint());
        return dto;
    }

    public static MovementPoint getMovementPoint() {
        MovementPoint point = new MovementPoint();
        point.setLatitude(MockingUtils.randInt(-90, 90));
        point.setLongitude(MockingUtils.randInt(-180,180));
        return point;
    }

    public static List<MovementSegment> getDtoList(Integer amount) {
        List<MovementSegment> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getDto(Long.valueOf(i)));
        }
        return dtoList;
    }
}
