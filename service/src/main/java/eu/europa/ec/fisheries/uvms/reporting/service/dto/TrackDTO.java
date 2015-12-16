package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import static javax.measure.unit.NonSI.KNOT;
import static javax.measure.unit.NonSI.NAUTICAL_MILE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.Setter;
import org.opengis.feature.simple.SimpleFeature;
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

    public TrackDTO(MovementTrack track, Vessel vessel) throws ReportingServiceException {
        this.track = track;
        asset = new AssetDTO(vessel);
        toGeometry(track.getWkt());
        computeEnvelope();
        computerNearestPoint();
    }

    public TrackDTO(MovementTrack track, Vessel vessel, DisplayFormat format) throws ReportingServiceException {
        this(track, vessel);

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

    public Geometry toGeometry(final String wkt) throws ReportingServiceException {
        if (wkt != null){
            WKTReader wktReader = new WKTReader();
            try {
                geometry = wktReader.read(wkt);
                return geometry;
            } catch (ParseException e) {
                throw new ReportingServiceException("ERROR WHILE PARSING GEOMETRY", e);
            }
        }
        return null;
    }
}
