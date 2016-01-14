package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import static javax.measure.unit.NonSI.KNOT;
import static javax.measure.unit.NonSI.NAUTICAL_MILE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.Setter;
import javax.measure.converter.UnitConverter;
import java.util.ArrayList;
import java.util.List;

public class TrackDTO {

    @JsonIgnore private MovementTrack track;
    @JsonIgnore private Geometry geometry;
    @JsonUnwrapped private AssetDTO asset;
    private List<Double> nearestPoint;
    private List<Double> extent;
    @Setter private UnitConverter velocityConverter = KNOT.getConverterTo(KNOT);
    @Setter private UnitConverter lengthConverter = NAUTICAL_MILE.getConverterTo(NAUTICAL_MILE);

    public TrackDTO(MovementTrack track, Asset asset) throws ReportingServiceException, ParseException {
        this.track = track;
        this.asset = new AssetDTO(asset);
        geometry = GeometryUtil.toGeometry(track.getWkt());
        computeEnvelope();
        computerNearestPoint();
    }

    public TrackDTO(MovementTrack track, Asset asset, DisplayFormat format) throws ReportingServiceException, ParseException {
        this(track, asset);

        if (format != null){
            lengthConverter = format.getLengthType().getConverter();
            velocityConverter = format.getVelocityType().getConverter();
        }

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

    public Double getTotalTimeAtSea() {

        return track.getTotalTimeAtSea();
    }

    public Double getDistance() {

        return lengthConverter.convert(track.getDistance() != null ? track.getDistance() : 0);
    }

    public Double getDuration() {

        return track.getDuration();
    }

    public String getId(){

        return  track.getId();

    }
}
