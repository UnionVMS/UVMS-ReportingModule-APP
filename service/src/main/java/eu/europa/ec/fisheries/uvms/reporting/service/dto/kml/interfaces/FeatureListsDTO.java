package eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureListsDTO {
    
    List<ActivityFeatureDTO> activities;
    List<PositionFeatureDto> positions;
    List<SegmentFeatureDTO> segments;
}
