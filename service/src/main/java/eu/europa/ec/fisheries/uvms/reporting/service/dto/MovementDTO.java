package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.measure.converter.UnitConverter;
import javax.xml.datatype.XMLGregorianCalendar;

import static javax.measure.unit.NonSI.*;

public class MovementDTO {

    public static final SimpleFeatureType SIMPLE_FEATURE_TYPE = buildFeatureType();
    private static final String GEOMETRY = "geometry", POSITION_TIME = "positionTime", SOURCE = "source";
    private static final String CONNECTION_ID = "connectionId", STATUS = "status", REPORTED_COURSE = "reportedCourse";
    private static final String MOVEMENT_TYPE = "movementType", ACTIVITY_TYPE = "activityType";
    private static final String REPORTED_SPEED = "reportedSpeed", CALCULATED_SPEED = "calculatedSpeed";
    private static final String COUNTRY_CODE = "countryCode", IRCS = "ircs", EXTERNAL_MARKING = "externalMarking";
    private static final String MOVEMENT_GUID = "movementGuid", MOVEMENT = "movement", NAME = "name", CFR = "cfr";

    @Delegate(types = Include.class) private MovementType movementType;
    @Setter private UnitConverter velocityConverter = KNOT.getConverterTo(KNOT);
    @Setter private UnitConverter lengthConverter = NAUTICAL_MILE.getConverterTo(NAUTICAL_MILE);

    private AssetDTO asset;

    public MovementDTO(MovementType movementType, Vessel vessel){
        this.movementType = movementType;
        asset = new AssetDTO(vessel);
    }

    public MovementDTO(MovementType movement, Vessel vessel, DisplayFormat format) {
        this(movement, vessel);

        if (format != null) {
            lengthConverter = format.getLengthType().getConverter();
            velocityConverter = format.getVelocityType().getConverter();
        }
    }

    private static SimpleFeatureType buildFeatureType() {

        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setName(MOVEMENT);
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.add(GEOMETRY, Point.class);
        sb.add(POSITION_TIME, String.class);
        sb.add(CONNECTION_ID, String.class);
        sb.add(STATUS, String.class);
        sb.add(REPORTED_COURSE, Double.class);
        sb.add(CALCULATED_SPEED, Double.class);
        sb.add(MOVEMENT_TYPE, String.class);
        sb.add(ACTIVITY_TYPE, String.class);
        sb.add(REPORTED_SPEED, Double.class);
        sb.add(CFR, String.class);
        sb.add(COUNTRY_CODE, String.class);
        sb.add(CALCULATED_SPEED, Double.class);
        sb.add(IRCS, String.class);
        sb.add(NAME, String.class);
        sb.add(MOVEMENT_GUID, String.class);
        sb.add(EXTERNAL_MARKING, String.class);
        sb.add(SOURCE, String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() throws ReportingServiceException {

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SIMPLE_FEATURE_TYPE);
        featureBuilder.set(GEOMETRY, toGeometry(getWkt()));
        featureBuilder.set(POSITION_TIME, getPositionTime());
        featureBuilder.set(CONNECTION_ID, getConnectId());
        featureBuilder.set(STATUS, getStatus());
        featureBuilder.set(REPORTED_COURSE, getReportedCourse());
        featureBuilder.set(MOVEMENT_GUID, getGuid());

        if (getMovementType() != null){
            featureBuilder.set(MOVEMENT_TYPE, getMovementType().value());
        }
        if (getActivity() != null && getActivity().getMessageType() != null){
            featureBuilder.set(ACTIVITY_TYPE, movementType.getActivity().getMessageType().value());
        }

        featureBuilder.set(REPORTED_SPEED, velocityConverter.convert(getReportedSpeed() != null ? getReportedSpeed() : 0));
        featureBuilder.set(CALCULATED_SPEED, velocityConverter.convert(getCalculatedSpeed() != null ? getCalculatedSpeed() : 0));
        featureBuilder.set(CFR, asset.getCfr());
        featureBuilder.set(COUNTRY_CODE, asset.getCountryCode());
        featureBuilder.set(IRCS, asset.getIrcs());
        featureBuilder.set(NAME, asset.getName());
        featureBuilder.set(EXTERNAL_MARKING, asset.getExternalMarking());
        featureBuilder.set(SOURCE, getSource());

        return featureBuilder.buildFeature(getGuid());
    }

    private Geometry toGeometry(final String wkt) throws ReportingServiceException {

        if (wkt != null){
            WKTReader wktReader = new WKTReader();
            try {
                return wktReader.read(wkt);
            } catch (ParseException e) {
                throw new ReportingServiceException("ERROR WHILE PARSING GEOMETRY", e);
            }
        }
        return null;
    }

    private interface Include {
        String getGuid();
        String getConnectId();
        String getStatus();
        Double getCalculatedSpeed();
        XMLGregorianCalendar getPositionTime();
        Double getReportedSpeed();
        MovementActivityType getActivity();
        Double getReportedCourse();
        String getWkt();
        MovementTypeType getMovementType();
        MovementSourceType getSource();
    }
}

