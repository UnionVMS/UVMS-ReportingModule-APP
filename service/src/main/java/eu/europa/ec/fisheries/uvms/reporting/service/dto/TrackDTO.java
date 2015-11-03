package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;
import org.opengis.feature.simple.SimpleFeature;

import java.util.ArrayList;
import java.util.List;

public class TrackDTO extends GeoJsonDTO {

    @Delegate(excludes = Exclude.class)
    private MovementTrack track;

    private AssetDTO asset;

    private List<Double> nearestPoint;

    private List<Double> extent;

    public TrackDTO(MovementTrack track, Vessel vessel) throws ReportingServiceException {
        this.track = track;
        asset = new AssetDTO(vessel);
        toGeometry(track.getWkt());
        computeEnvelope();
        computerNearestPoint();
    }

    private void computerNearestPoint() {
        nearestPoint = new ArrayList<>();
        if (geometry !=null && geometry.getCentroid() != null){
            DistanceOp distanceOp = new DistanceOp(geometry.getCentroid(), geometry);
            Coordinate[] nearestPoints = distanceOp.nearestPoints();
            nearestPoint.add(nearestPoints[0].x);
            nearestPoint.add(nearestPoints[0].y);
        }
    }

    @Override
    public SimpleFeature toFeature() throws ReportingServiceException {
        return null;
    }

    private interface Exclude {
        String getWkt();
        Double getTotalTimeAtSea();
    }

    private void computeEnvelope() {
        extent = new ArrayList<>();
        if (geometry != null && geometry.getEnvelopeInternal() != null){
            Envelope internal = geometry.getEnvelopeInternal();
            extent.add(internal.getMinX());
            extent.add(internal.getMinY());
            extent.add(internal.getMaxX());
            extent.add(internal.getMaxY());
        }
    }

    public List<Double> getNearestPoint() {
        return nearestPoint;
    }

    public AssetDTO getAsset() {
        return asset;
    }

    public List<Double> getExtent() {
        return extent;
    }

}
