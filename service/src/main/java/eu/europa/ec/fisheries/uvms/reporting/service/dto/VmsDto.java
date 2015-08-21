package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VmsDto {

    private static final String GEOMETRY = "geometry";

    public static final SimpleFeatureType MOVEMENT_TYPE;
    public static final SimpleFeatureType SEGMENT_TYPE;
    public static final String POSITION_TIME = "positionTime";
    public static final String MEASURED_SPEED = "measuredSpeed";
    public static final String IRCS = "ircs";
    public static final String CFR = "cfr";
    public static final String STATUS = "status";
    public static final String COURSE = "course";
    public static final String MESSAGE_TYPE = "messageType";
    public static final String CALCULATED_SPEED = "calculatedSpeed";
    public static final String CONNECT_ID = "connectId";
    public static final String COUNTRY = "countryCode";
    public static final String VESSEL_ID = "vesselId";
    public static final String AVERAGE_SPEED = "averageSpeed";
    public static final String AVERAGE_COURSE = "averageCourse";

    static {
        MOVEMENT_TYPE = buildMovementType();
        SEGMENT_TYPE = buildSegmentType();
    }

    private Map<AssetDto,List<MovementDto>> movements;
    private Map<AssetDto,List<SegmentDto>> segments;

    public Map<AssetDto, List<MovementDto>> getMovements() {
        return movements;
    }

    public void setMovements(Map<AssetDto, List<MovementDto>> movements) {
        this.movements = movements;
    }

    public Map<AssetDto, List<SegmentDto>> getSegments() {
        return segments;
    }

    public void setSegments(Map<AssetDto, List<SegmentDto>> segments) {
        this.segments = segments;
    }

    private static SimpleFeatureType buildSegmentType() {

        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName("Segments");
        sb.add(GEOMETRY, LineString.class);
        sb.add(AVERAGE_SPEED, BigDecimal.class);
        sb.add(AVERAGE_COURSE, BigDecimal.class);
        sb.add(IRCS, String.class);
        sb.add(CFR, String.class);
        sb.add(VESSEL_ID, String.class);
        sb.add(COUNTRY, String.class);
        return sb.buildFeatureType();

    }

    private static SimpleFeatureType buildMovementType() {

        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setName("Movement");
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.add(GEOMETRY, Point.class);
        sb.add(POSITION_TIME, Date.class);
        sb.add(MEASURED_SPEED, BigDecimal.class);
        sb.add(IRCS, String.class);
        sb.add(CFR, String.class);
        sb.add(VESSEL_ID, String.class);
        sb.add(CONNECT_ID, String.class);
        sb.add(COURSE, Integer.class);
        sb.add(STATUS, String.class);
        sb.add(COUNTRY, String.class);
        sb.add(MESSAGE_TYPE, String.class);
        sb.add(CALCULATED_SPEED, BigDecimal.class);
        return sb.buildFeatureType();

    }

}
