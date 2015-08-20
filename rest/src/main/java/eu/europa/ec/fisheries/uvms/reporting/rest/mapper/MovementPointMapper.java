package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * //TODO create test
 */

public abstract class MovementPointMapper {

    // FIXME this should be WKT
    public static MovementPointMapper INSTANCE = Mappers.getMapper(MovementPointMapper.class);

    @Mapping(target = "geometry", expression = "java(geometryToGeoJson(movementPoint))")
    abstract public Geometry movementPointToGeometry(MovementPoint movementPoint);

    protected Geometry geometryToGeoJson(MovementPoint movementPoint) {
        Coordinate coordinate = new Coordinate(movementPoint.getLongitude(), movementPoint.getLatitude());
        return new GeometryFactory().createPoint(coordinate);
    }
}
