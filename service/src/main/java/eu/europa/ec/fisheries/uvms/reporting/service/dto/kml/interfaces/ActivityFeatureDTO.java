package eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces;

import java.util.List;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityFeatureDTO {
    private int activityId;
    private int faReportID;
    private String activityType;
    private String geometry;
    private String acceptedDateTime;
    private String dataSource;
    private String reportType;
    private String purposeCode;
    private String vesselName;
    private String vesselGuid;
    private String tripId;
    private String flagState;
    private boolean isCorrection;
    private List<String> gears;
    private List<String> species;
    private List<String> ports;
    private List<String> areas;
    private List<VesselIdentifierType> vesselIdentifiers;
}
