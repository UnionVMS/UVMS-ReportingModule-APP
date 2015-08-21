package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * //TODO add test
 */
@Mapper(componentModel = "cdi")
public abstract class MovementBaseTypeMapper {

    public static MovementBaseTypeMapper INSTANCE = Mappers.getMapper(MovementBaseTypeMapper.class);

    @Mapping(target = "geometry", expression = "java(geometryToGeoJson(movementBaseType.getPosition()))")
    abstract public MovementDto movementBaseTypeToMovementDto(MovementBaseType movementBaseType);

    protected Geometry geometryToGeoJson(MovementPoint movementPoint) {
        Coordinate coordinate = new Coordinate(movementPoint.getLongitude(), movementPoint.getLatitude());
        return new GeometryFactory().createPoint(coordinate);
    }
}

