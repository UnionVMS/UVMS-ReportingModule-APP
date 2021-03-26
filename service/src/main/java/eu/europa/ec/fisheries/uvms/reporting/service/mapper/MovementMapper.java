/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Movement;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Segment;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Track;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper
public abstract class MovementMapper {

    @Mappings({
            @Mapping(target = "position", expression = "java(getPoint(movementType.getPosition()))"),
            @Mapping(target = "source", expression = "java(getEnumVal(movementType.getSource()))"),
            @Mapping(target = "movementType", expression = "java(getEnumVal(movementType.getMovementType()))")
    })
    public abstract Movement toMovement(MovementType movementType);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "movementGuid", ignore = true),
            @Mapping(target = "segmentCategory", expression = "java(getEnumVal(movementSegment.getCategory()))"),
            @Mapping(target = "segment", expression = "java(getLineString(movementSegment.getWkt()))"),
            @Mapping(target = "calculatedSpeed", expression = "java(calculateSpeed(movementSegment))")
    })
    public abstract Segment toSegment(MovementSegment movementSegment);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "nearestPoint", ignore = true),
            @Mapping(target = "extent", ignore = true)
    })
    public abstract Track toTrack(MovementTrack movementTrack);

    protected String getEnumVal(Enum<?> type) {
        if (type == null) {
            return null;
        }
        return type.name();
    }

    protected Point getPoint(MovementPoint movementPoint) {
        return (Point) GeometryUtils.createPoint(movementPoint.getLongitude(),movementPoint.getLatitude());
    }

    @SneakyThrows
    protected LineString getLineString(String wtk) {
        return GeometryUtil.toLineString(wtk);
    }

    protected Double calculateSpeed(MovementSegment movementSegment) {
        try {
            return movementSegment.getDistance() / movementSegment.getDuration();
        } catch (Exception e) {
            return null;
        }
    }
}