package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportResult {

    @Id
    @Column(name = "id")
    private Long id;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "position_coordinates", columnDefinition = "Geometry")
    private Point position;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "position_time")
    private Date positionTime;

    @Column(name = "connect_id")
    private String connectId;

    @Column(name = "movement_guid")
    private String movementGuid;

    @Column(name = "source")
    private String source;

    @Column(name = "movement_type")
    private String movementType;

    @Column(name = "movement_activity_type")
    private String movementActivityType;

    @Column(name = "reported_course")
    private Double reportedCourse;

    @Column(name = "reported_speed")
    private Double reportedSpeed;

    @Column(name = "calculated_speed")
    private Double calculatedSpeed;

    @Column(name = "closest_country")
    private String closestCountry;

    @Column(name = "closest_country_distance")
    private Double closestCountryDistance;

    @Column(name = "closest_port")
    private String closestPort;

    @Column(name = "closest_port_distance")
    private Double closestPortDistance;

    // asset data

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

    // track data
    @Column(name = "nearest_point_coordinates")
    private String trackNearestPoint;

    @Column(name = "extent_coordinates")
    private String trackExtent;

    @Column(name = "duration")
    private Double trackDuration;

    @Column(name = "distance")
    private Double trackDistance;

    @Column(name = "total_time_at_sea")
    private Double trackTotalTimeAtSea;


    // segment data
    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "segment_id")
    private Long segmentId;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "segment_coordinates", columnDefinition = "Geometry")
    private LineString segmentCoordinates;

    @Column(name = "segment_category")
    private String segmentCategory;

    @Column(name = "segment_course_over_ground")
    private Double segmentCourseOverGround;

    @Column(name = "segment_speed_over_ground")
    private Double segmentSpeedOverGround;

    @Column(name = "segment_calculated_speed")
    private Double segmanentCalculatedSpeed;

    @Column(name = "segment_duration")
    private Double segmentDuration;

    @Column(name = "segment_distance")
    private Double segmentDistance;


}
