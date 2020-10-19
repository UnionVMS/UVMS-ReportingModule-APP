package eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentFeatureDTO {
    
    List<String> geometry;
    String cfr;
    String countryCode;
    String courseOverGround;
    String distance;
    String duration;
    String externalMarking;
    String ircs;
    String name;
    String segmentCategory;
    String speedOverGround;
    String trackId;
    String color;
}
