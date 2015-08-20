package eu.europa.ec.fisheries.uvms.reporting.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import java.io.IOException;

/**
 * //TODO create test
 */
public class SegmentDtoSerializer extends JsonSerializer<SegmentDto> {

    @Override
    public void serialize(SegmentDto segmentDto, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        gen.writeStartObject();
       // gen.writeStringField(GeoJSON.TYPE.getValue(), GeoJSON.FEATURE.getValue());
       // gen.writeFieldName(GeoJSON.GEOMETRY.getValue());

        Geometry presentPosition = segmentDto.getPresentPosition();
        Geometry previousPosition = segmentDto.getPreviousPosition();

        Coordinate[] coords  =
                new Coordinate[] {segmentDto.getPresentPosition().getCoordinate() ,
                        segmentDto.getPreviousPosition().getCoordinate()};

        LineString lineString = new GeometryFactory().createLineString(coords);
       // GeoJSONSerializerUtil.writeLineString(gen, lineString);
        writeProperties(gen, segmentDto);
        gen.writeEndObject();

    }

    private void writeProperties(JsonGenerator gen, SegmentDto segmentDto) throws IOException {

        //gen.writeFieldName(GeoJSON.PROPERTIES.getValue());
        gen.writeStartObject();
        gen.writeStringField("averageCourse", String.valueOf(segmentDto.getAverageCourse()));
        gen.writeStringField("averageSpeed", String.valueOf(segmentDto.getAverageSpeed()));
        gen.writeEndObject();
    }
}
