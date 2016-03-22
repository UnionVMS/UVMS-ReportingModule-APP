package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import static javax.measure.unit.NonSI.KNOT;
import static javax.measure.unit.NonSI.NAUTICAL_MILE;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import javax.measure.converter.UnitConverter;

public class SegmentDTO {

    public static final SimpleFeatureType SEGMENT = build();
    private static final String GEOMETRY = "geometry";
    private static final String COURSE_OVER_GROUND = "courseOverGround";
    private static final String SPEED_OVER_GROUND = "speedOverGround";
    private static final String DURATION = "duration";
    private static final String DISTANCE = "distance";
    private static final String TRACK_ID = "trackId";
    private static final String CFR = "cfr";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String IRCS = "ircs";
    private static final String NAME = "name";
    private static final String SEGMENTS = "segments";
    private static final String SEGMENT_CATEGORY_TYPE = "segmentCategory";
    private static final String EXTERNAL_MARKING = "externalMarking";
    private AssetDTO asset;

    @Setter private UnitConverter velocityConverter = KNOT.getConverterTo(KNOT);
    @Setter private UnitConverter lengthConverter = NAUTICAL_MILE.getConverterTo(NAUTICAL_MILE);

    @Delegate(types = Include.class)
    private MovementSegment segment;

    public SegmentDTO(MovementSegment segment, Asset asset) {
        this.asset = new AssetDTO(asset);
        this.segment = segment;
    }

    public SegmentDTO(MovementSegment segment, Asset asset, DisplayFormat format) {

        this(segment, asset);

        if (format != null) {

            lengthConverter = format.getLengthType().getConverter();
            velocityConverter = format.getVelocityType().getConverter();

        }

    }

    private static SimpleFeatureType build(){
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName(SEGMENTS);
        sb.add(GEOMETRY, LineString.class);
        sb.add(COURSE_OVER_GROUND, Double.class);
        sb.add(SPEED_OVER_GROUND, Double.class);
        sb.add(DURATION, Double.class);
        sb.add(DISTANCE, Double.class);
        sb.add(CFR, String.class);
        sb.add(COUNTRY_CODE, String.class);
        sb.add(IRCS, String.class);
        sb.add(NAME, String.class);
        sb.add(TRACK_ID, String.class);
        sb.add(SEGMENT_CATEGORY_TYPE, String.class);
        sb.add(EXTERNAL_MARKING, String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() throws ReportingServiceException, ParseException {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SEGMENT);
        featureBuilder.set(SPEED_OVER_GROUND, velocityConverter.convert(getSpeedOverGround() != null ? getSpeedOverGround() : 0));
        featureBuilder.set(COURSE_OVER_GROUND, getCourseOverGround());
        featureBuilder.set(GEOMETRY, GeometryUtil.toGeometry(getWkt()));
        featureBuilder.set(DISTANCE, lengthConverter.convert(getDistance() != null ? getDistance() : 0));
        featureBuilder.set(DURATION, getDuration());
        featureBuilder.set(TRACK_ID, getTrackId());
        featureBuilder.set(CFR, asset.getCfr());
        featureBuilder.set(IRCS, asset.getIrcs());
        featureBuilder.set(EXTERNAL_MARKING, asset.getExternalMarking());
        featureBuilder.set(COUNTRY_CODE, asset.getCountryCode());
        featureBuilder.set(NAME, asset.getName());
        featureBuilder.set(SEGMENT_CATEGORY_TYPE, getCategory());
        return featureBuilder.buildFeature(String.valueOf(getId()));
    }

    private interface Include {
        String getId();
        Double getCourseOverGround();
        Double getSpeedOverGround();
        Double getDuration();
        Double getDistance();
        String getWkt();
        String getTrackId();
        SegmentCategoryType getCategory();
    }

}
