package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
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

    @Delegate(types = Include.class)
    private MovementSegment segment;

    private Geometry geometry;

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
        sb.add("crs_o_gnd", BigDecimal.class);
        sb.add("spd_o_gnd", BigDecimal.class);
        sb.add("dur", BigDecimal.class);
        sb.add("dist", BigDecimal.class);
        sb.add("cfr", String.class);
        sb.add("cc", String.class);
        sb.add("ircs", String.class);
        sb.add("name", String.class);
        sb.add("guid", String.class);
        sb.add("color", String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature(){
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SEGMENT);
        featureBuilder.add(getGeometry());
        featureBuilder.add(getCourseOverGround());
        featureBuilder.add(getSpeedOverGround());
        featureBuilder.add(getDuration());
        featureBuilder.add(getDistance());
        featureBuilder.add(asset.getCfr());
        featureBuilder.add(asset.getCountryCode());
        featureBuilder.add(asset.getIrcs());
        featureBuilder.add(asset.getName());
        featureBuilder.add(asset.getVesselId().getGuid());
        featureBuilder.add(asset.getColor());
        return featureBuilder.buildFeature(String.valueOf(getId()));
    }

    private interface Include {
        String getId();
        BigDecimal getCourseOverGround();
        BigDecimal getSpeedOverGround();
        BigDecimal getDuration();
        BigDecimal getDistance();
    }

    private Geometry toGeometry(final MovementSegment segment) {
        WKTReader wktReader = new WKTReader();
        try {
            return wktReader.read(segment.getWkt());
        } catch (ParseException e) {
            e.printStackTrace(); // FIXME
        }
        return null;
    }

    public Geometry getGeometry() {
        return geometry;
    }

}
