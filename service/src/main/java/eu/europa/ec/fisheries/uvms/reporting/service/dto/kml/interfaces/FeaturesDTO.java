package eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeaturesDTO {
    
    Map<String, Map<String, FeatureListsDTO>> features;
}
