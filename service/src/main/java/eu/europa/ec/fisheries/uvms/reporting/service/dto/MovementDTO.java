/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.measure.converter.UnitConverter;
import java.util.Date;

import static javax.measure.unit.NonSI.KNOT;
import static javax.measure.unit.NonSI.NAUTICAL_MILE;

public class MovementDTO {

    public static final SimpleFeatureType SIMPLE_FEATURE_TYPE = buildFeatureType();
    private static final String GEOMETRY = "geometry", POSITION_TIME = "positionTime", SOURCE = "source";
    private static final String CONNECTION_ID = "connectionId", STATUS = "status", REPORTED_COURSE = "reportedCourse";
    private static final String MOVEMENT_TYPE = "movementType", ACTIVITY_TYPE = "activityType";
    private static final String REPORTED_SPEED = "reportedSpeed", CALCULATED_SPEED = "calculatedSpeed";
    private static final String COUNTRY_CODE = "countryCode", IRCS = "ircs", EXTERNAL_MARKING = "externalMarking";
    private static final String MOVEMENT_GUID = "movementGuid", MOVEMENT = "movement", NAME = "name", CFR = "cfr";
    private static final String TRIP_ID = "tripId";

    @Delegate(types = Include.class) private MovementType movementType;
    @Setter private UnitConverter velocityConverter = KNOT.getConverterTo(KNOT);
    @Setter private UnitConverter lengthConverter = NAUTICAL_MILE.getConverterTo(NAUTICAL_MILE);

    private AssetDTO asset;

    public MovementDTO(MovementType movementType, Asset asset){
        this.movementType = movementType;
        this.asset = new AssetDTO(asset);
    }

    public MovementDTO(MovementType movement, Asset asset, DisplayFormat format) {
        this(movement, asset);

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
        sb.add(TRIP_ID, String.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() throws ReportingServiceException, ParseException {

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SIMPLE_FEATURE_TYPE);
        featureBuilder.set(GEOMETRY, GeometryUtil.toGeometry(getWkt()));
        featureBuilder.set(POSITION_TIME, getPositionTime() != null ? DateFormatUtils.format(getPositionTime(), DateUtils.DATE_TIME_UI_FORMAT) : null);
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

    private interface Include {
        String getGuid();
        String getConnectId();
        String getStatus();
        Double getCalculatedSpeed();
        Date getPositionTime();
        Double getReportedSpeed();
        MovementActivityType getActivity();
        Double getReportedCourse();
        String getWkt();
        MovementTypeType getMovementType();
        MovementSourceType getSource();
    }
}