package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockVesselData;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * //TODO create test
 */

public class TrackDto {

    @Delegate(types = Include.class)
    @JsonIgnore
    private MovementTrack track;

    private AssetDto asset;

    @JsonIgnore
    private Geometry geometry;

    private List<Double> nearestPoint;

    private List<Double> extent;

    public TrackDto(MovementTrack track, Vessel vessel){
        this.track = track;
        asset = new AssetDto(vessel);
        asset.setColor(MockVesselData.COLORS.get(MockingUtils.randInt(0,6)));// FIXME mock
        geometry = toGeometry();
        computeEnvelope();
        computerNearestPoint();
    }

    private void computerNearestPoint() {
        nearestPoint = new ArrayList<>();
        DistanceOp distanceOp = new DistanceOp(geometry.getCentroid(), geometry);
        Coordinate[] nearestPoints = distanceOp.nearestPoints();
        nearestPoint.add(nearestPoints[0].x);
        nearestPoint.add(nearestPoints[0].y);
    }

    private interface Include {
        String getId();
    }

    private Geometry toGeometry() {
        WKTReader wktReader = new WKTReader();
        try {
            return wktReader.read(track.getWkt());
        } catch (ParseException e) {
            e.printStackTrace(); // FIXME
        }
        return null;
    }

    private void computeEnvelope() {
        extent = new ArrayList<>();
        Envelope internal = geometry.getEnvelopeInternal();
        extent.add(internal.getMaxX());
        extent.add(internal.getMaxY());
        extent.add(internal.getMinX());
        extent.add(internal.getMinY());
    }

    public List<Double> getNearestPoint() {
        return nearestPoint;
    }

    public AssetDto getAsset() {
        return asset;
    }

    public List<Double> getExtent() {
        return extent;
    }

}
