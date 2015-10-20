package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockVesselData;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigDecimal;

public class MovementDTO {

    public static final SimpleFeatureType MOVEMENT = buildFeatueType();

    @Delegate(types = Include.class)
    private MovementType movementType;

    private AssetDTO asset;

    private String positionTime;

    public MovementDTO(MovementType movementType, Vessel vessel){
        this.movementType = movementType;
        asset = new AssetDTO(vessel);
        asset.setColor(MockVesselData.COLORS.get(MockingUtils.randInt(0, 9)));// FIXME only for mock
    }

    private static SimpleFeatureType buildFeatueType() {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setName("Movement");
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.add("geometry", Point.class);
        sb.add("pos_tm", String.class);
        //sb.add("m_spd", BigDecimal.class);
        sb.add("con_id", String.class);
        sb.add("stat", String.class);
        sb.add("crs", Double.class);
        sb.add("msg_tp", String.class);
        sb.add("c_spd", Double.class);
        sb.add("cfr", String.class);
        sb.add("cc", String.class);
        sb.add("ircs", String.class);
        sb.add("name", String.class);
        sb.add("guid", String.class);
        sb.add("color", String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature(){
        // FIXME add sanity check
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(MOVEMENT);
        featureBuilder.add(getGeometry());
        featureBuilder.add(getPositionTime());
        featureBuilder.add(getConnectId());
        featureBuilder.add(getStatus());
        featureBuilder.add(getCalculatedCourse());
        if (getMovementType() != null){
            featureBuilder.add(getMovementType().value());
        }
        featureBuilder.add(getCalculatedSpeed());
        featureBuilder.add(asset.getCfr());
        featureBuilder.add(asset.getCountryCode());
        featureBuilder.add(asset.getIrcs());
        featureBuilder.add(asset.getName());
        if (asset.getVesselId() != null){
            featureBuilder.add(asset.getVesselId().getGuid());
        }
        featureBuilder.add(asset.getColor());
        return featureBuilder.buildFeature(getGuid());
    }

    private interface Include {
        String getGuid();
        void setGuid(String id);
        String getConnectId();
        String getStatus();
        Double getCalculatedSpeed();
        Double getCalculatedCourse();
        String getWkt();
        MovementTypeType getMovementType();
    }

    public String getPositionTime() {
        return positionTime;
    }

    public Geometry getGeometry() {
        WKTReader wktReader = new WKTReader(); // FIXME check if wktReader is thread safe?
        try {
            return wktReader.read(getWkt());
        } catch (ParseException e) {
            e.printStackTrace(); // FIXME
        }
        return null;
    }
}

