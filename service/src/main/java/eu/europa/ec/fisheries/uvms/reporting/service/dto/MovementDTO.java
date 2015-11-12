package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.xml.datatype.XMLGregorianCalendar;

public class MovementDTO extends GeoJsonDTO {

    public static final SimpleFeatureType MOVEMENTYPE = buildFeatureType();

    private static final String POSITION_TIME = "positionTime";
    private static final String CONNECTION_ID = "connectionId";
    private static final String STATUS = "status";
    private static final String REPORTED_COURSE = "reportedCourse";
    private static final String MOVEMENT_TYPE = "movementType";
    private static final String ACTIVITY_TYPE = "activityType";
    private static final String REPORTED_SPEED = "reportedSpeed";
    private static final String CALCULATED_SPEED = "calculatedSpeed";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String IRCS = "ircs";
    private static final String CFR = "cfr";
    private static final String NAME = "name";
    private static final String MOVEMENT_GUID = "movementGuid";
    private static final String MOVEMENT = "movement";
    private static final String EXTERNAL_MARKING = "externalMarking";

    @Delegate(types = Include.class)
    private MovementType movementType;

    private AssetDTO asset;

    public MovementDTO(MovementType movementType, Vessel vessel){
        this.movementType = movementType;
        asset = new AssetDTO(vessel);
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
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() throws ReportingServiceException {

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(MOVEMENTYPE);
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

        featureBuilder.set(REPORTED_SPEED, getReportedSpeed());
        featureBuilder.set(CALCULATED_SPEED, getCalculatedSpeed());
        featureBuilder.set(CFR, asset.getCfr());
        featureBuilder.set(COUNTRY_CODE, asset.getCountryCode());
        featureBuilder.set(IRCS, asset.getIrcs());
        featureBuilder.set(NAME, asset.getName());
        featureBuilder.set(EXTERNAL_MARKING, asset.getExternalMarking());

        return featureBuilder.buildFeature(getGuid());
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
    }
}

