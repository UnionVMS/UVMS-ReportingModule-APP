package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.LineString;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigDecimal;

public class SegmentDTO extends GeoJsonDTO {

    public static final SimpleFeatureType SEGMENT = build();

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

    @Delegate(types = Include.class)
    private MovementSegment segment;

    public SegmentDTO(MovementSegment segment, Vessel vessel) {
        asset = new AssetDTO(vessel);
        this.segment = segment;
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

    @Override
    public SimpleFeature toFeature() throws ReportingServiceException {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SEGMENT);
        featureBuilder.set(SPEED_OVER_GROUND, getSpeedOverGround());
        featureBuilder.set(COURSE_OVER_GROUND, getCourseOverGround());
        featureBuilder.set(GEOMETRY, toGeometry(getWkt()));
        featureBuilder.set(DISTANCE, getDistance());
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
