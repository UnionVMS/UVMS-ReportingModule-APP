package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.schema.movement.v1.MessageType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.temp.MockVesselData;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.joda.time.DateTime;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.math.BigDecimal;

public class MovementDto {

    public static final SimpleFeatureType MOVEMENT = build();

    @Delegate(types = Include.class)
    private MovementBaseType movementBaseType;

    private AssetDto asset;

    private String positionTime;

    public MovementDto(MovementBaseType movementBaseType, Vessel vessel){
        this.movementBaseType = movementBaseType;
        asset = new AssetDto(vessel);
        asset.setColor(MockVesselData.COLORS.get(MockingUtils.randInt(0, 9)));// FIXME only for mock
    }

    private static SimpleFeatureType build() {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setName("Movement");
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.add("geometry", Point.class);
        sb.add("pos_tm", String.class);
        sb.add("m_spd", BigDecimal.class);
        sb.add("con_id", String.class);
        sb.add("stat", String.class);
        sb.add("crs", Integer.class);
        sb.add("msg_tp", String.class);
        sb.add("c_spd", BigDecimal.class);
        sb.add("cfr", String.class);
        sb.add("cc", String.class);
        sb.add("ircs", String.class);
        sb.add("name", String.class);
        sb.add("guid", String.class);
        sb.add("color", String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature(){
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(MOVEMENT);
        featureBuilder.add(getGeometry());
        featureBuilder.add(DateUtils.UI_FORMATTER.print(new DateTime(getPositionTime())));
        featureBuilder.add(getMeasuredSpeed());
        featureBuilder.add(getConnectId());
        featureBuilder.add(getStatus());
        featureBuilder.add(getCourse());
        featureBuilder.add(getMessageType());
        featureBuilder.add(getCalculatedSpeed());
        featureBuilder.add(asset.getCfr());
        featureBuilder.add(asset.getCountryCode());
        featureBuilder.add(asset.getIrcs());
        featureBuilder.add(asset.getName());
        featureBuilder.add(asset.getVesselId().getGuid());
        featureBuilder.add(asset.getColor());
        return featureBuilder.buildFeature(getId());
    }

    private interface Include {
        String getId();
        void setId(String id);
        String getConnectId();
        String getStatus();
        BigDecimal getMeasuredSpeed();
        BigDecimal getCalculatedSpeed();
        int getCourse();
        MessageType getMessageType();
    }

    public String getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(String positionTime) {
        this.positionTime = positionTime;
    }

    public Geometry getGeometry() {
        Coordinate coordinate = new Coordinate(movementBaseType.getPosition().getLongitude(), movementBaseType.getPosition().getLatitude());
        return new GeometryFactory().createPoint(coordinate);
    }
}

