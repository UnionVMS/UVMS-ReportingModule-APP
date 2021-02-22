package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.vividsolutions.jts.geom.MultiPoint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityReportResult {


    //Activity
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "act_id")
    private String activityId;
    @Column(name = "fa_report_id")
    private String faReportId;
    @Column(name = "trip_id")
    private String tripId;
    @Column(name = "report_type")
    private String reportType;
    @Column(name = "activity_type")
    private String activityType;
    @Column(name = "purpose_code")
    private String purposeCode;
    @Column(name = "source")
    private String source;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accepted_timestamp")
    private Date acceptedDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "calculated_timestamp")
    private Date calculatedDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "occurrence_timestamp")
    private Date occurrenceDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_timestamp")
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_timestamp")
    private Date endDate;
    @Column(name = "status")
    private String status;
    @Column(name = "is_latest")
    private Boolean latest;
    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "activity_coordinates", columnDefinition = "Geometry")
    private MultiPoint activityCoordinates;
    @Column(name = "activity_latitude")
    private Double latitude;
    @Column(name = "activity_longitude")
    private Double longitude;
    @Column(name = "master")
    private String master;
    @Column(name = "reason_code")
    private String reasonCode;
    @Column(name = "activity_assetHistGuid")
    private String activity_assetHistGuid;

    //Asset
    @Column(name = "asset_hist_guid")
    private String assetHistGuid;
    @Column(name = "asset_guid")
    private String assetGuid;
    @Column(name = "asset_hist_active")
    private Boolean assetHistActive;
    @Column(name = "iccat")
    private String iccat;
    @Column(name = "uvi")
    private String uvi;
    @Column(name = "cfr")
    private String cfr;
    @Column(name = "ircs")
    private String ircs;
    @Column(name = "name")
    private String name;
    @Column(name = "ext_mark")
    private String externalMarking;
    @Column(name = "gfcm")
    private String gfcm;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "length_overall")
    private Double lengthOverall;
    @Column(name = "main_gear_type")
    private String mainGearType;

    //Trip
    @Column(name = "trip_trip_id")
    private Long trip_trip_id;
    @Column(name = "trip_tripid")
    private String trip_tripid;
    @Column(name = "trip_id_scheme")
    private String tripIdScheme;
    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "trip_coordinates", columnDefinition = "Geometry")
    private MultiPoint coordinates;
    @Column(name = "first_fishing_activity")
    private String firstFishingActivity;
    @Column(name = "first_fishing_activity_timestamp")
    private Date firstFishingActivityDate;
    @Column(name = "last_fishing_activity")
    private String lastFishingActivity;
    @Column(name = "last_fishing_activity_timestamp")
    private Date lastFishingActivityDate;
    @Column(name = "trip_duration")
    private Double tripDuration;
    @Column(name = "number_of_corrections")
    private Integer numberOfCorrections;
    @Column(name = "number_of_positions")
    private Integer positionCount;
    @Column(name = "trip_assetHistGuid")
    private String trip_assetHistGuid;

    //Activity Location
    @Column(name = "type")
    private String locationType;
    @Column(name = "type_code")
    private String locationTypeCode;

    //Activity Gear
    @Column(name = "gear_code")
    private String gear;

    //Activity catch location
    @Column(name = "catch_location_type")
    private String catchLocationType;
    @Column(name = "catch_location_code")
    private String locationCode;

    //Activity species code
    @Column(name = "species_code")
    private String speciesCode;
}
