package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.LineString;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
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

    private AssetDTO asset;

    @Delegate(types = Include.class)
    private MovementSegment segment;

    public SegmentDTO(MovementSegment segment, Vessel vessel) {
        asset = new AssetDTO(vessel);
        //asset.setColor(MockVesselData.COLORS.get(MockingUtils.randInt(0, 9)));// FIXME only for mock
        this.segment = segment;
    }

    private static SimpleFeatureType build(){
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName("Segments");
        sb.add(GEOMETRY, LineString.class);
        sb.add(COURSE_OVER_GROUND, BigDecimal.class);
        sb.add(SPEED_OVER_GROUND, BigDecimal.class);
        sb.add(DURATION, BigDecimal.class);
        sb.add(DISTANCE, BigDecimal.class);
        sb.add("cfr", String.class);
        sb.add("countryCode", String.class);
        sb.add("ircs", String.class);
        sb.add("name", String.class);
        //sb.add("guid", String.class);
        //sb.add("color", String.class);
        sb.add(TRACK_ID, String.class);
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
        featureBuilder.set("cfr", asset.getCfr());
        featureBuilder.set("ircs", asset.getIrcs());
        featureBuilder.set("countryCode", asset.getCountryCode());
        featureBuilder.set("name", asset.getName());
        //featureBuilder.set("color", asset.getColor());
        //featureBuilder.add(asset.getVesselId().getGuid());
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
    }

}
