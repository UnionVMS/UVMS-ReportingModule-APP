package eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionFeatureDto {
    
    String geometry;
    String positionTime;
    String connectionId;
    String reportedCourse;
    String movementType;
    String reportedSpeed;
    String cfr;
    String countryCode;
    String calculatedSpeed;
    String ircs;
    String name;
    String movementGuid;
    String externalMarking;
    String source;
    String isVisible;
    String color;
}
