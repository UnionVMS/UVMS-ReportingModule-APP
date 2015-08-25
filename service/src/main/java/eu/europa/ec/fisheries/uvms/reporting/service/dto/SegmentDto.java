package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigDecimal;

public class SegmentDto {

    public static final SimpleFeatureType SEGMENT = build();
    private AssetDto asset;

    @Delegate
    private MovementSegment segment;

    private LineString geometry;
    private BigDecimal averageSpeed;
    private BigDecimal averageCourse;

    public SegmentDto(MovementSegment segment, Vessel vessel) {
        asset = new AssetDto(vessel);
        geometry = toGeometry(segment);
        this.segment = segment;
    }

    private static SimpleFeatureType build(){
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName("Segments");
        sb.add("geometry", LineString.class);
        sb.add("avg_spd", BigDecimal.class);
        sb.add("avg_crs", BigDecimal.class);
        sb.add("cfr", String.class);
        sb.add("cc", String.class);
        sb.add("ircs", String.class);
        sb.add("name", String.class);
        sb.add("guid", String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature(){
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SEGMENT);
        featureBuilder.add(getGeometry());
        featureBuilder.add(getAverageSpeed());
        featureBuilder.add(getAverageCourse());
        featureBuilder.add(asset.getCfr());
        featureBuilder.add(asset.getCountryCode());
        featureBuilder.add(asset.getIrcs());
        featureBuilder.add(asset.getName());
        featureBuilder.add(asset.getVesselId().getGuid());
        return featureBuilder.buildFeature(String.valueOf(getId()));
    }

    private LineString toGeometry(final MovementSegment segment) {
        Coordinate coordinate = new Coordinate(segment.getPresentPosition().getLongitude(), segment.getPresentPosition().getLatitude());
        Coordinate coordinate1 = new Coordinate(segment.getPreviousPosition().getLongitude(), segment.getPreviousPosition().getLatitude());
        Coordinate[] coords = {coordinate, coordinate1};
        return new GeometryFactory().createLineString(coords);
    }

    private interface Include {

    }

    public LineString getGeometry() {
        return geometry;
    }

    public BigDecimal getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(BigDecimal averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public BigDecimal getAverageCourse() {
        return averageCourse;
    }

    public void setAverageCourse(BigDecimal averageCourse) {
        this.averageCourse = averageCourse;
    }
}
