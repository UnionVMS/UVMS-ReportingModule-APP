package eu.europa.ec.fisheries.uvms.reporting.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementPointDto;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import java.io.IOException;

/**
 * //TODO create test
 */
public class MovementPointDtoSerializer extends JsonSerializer<MovementPointDto> {

    @Override
    public void serialize(MovementPointDto movementPointDto, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        gen.writeStartObject();
     //   gen.writeStringField(GeoJSON.TYPE.getValue(), GeoJSON.FEATURE.getValue());
     //   gen.writeFieldName(GeoJSON.GEOMETRY.getValue());

        if (movementPointDto != null){
            double latitude = movementPointDto.getLatitude();
            double longitude = movementPointDto.getLongitude();
            Point point = new Point();
            LngLatAlt coordinates = new LngLatAlt();
            coordinates.setLatitude(latitude);
            coordinates.setLongitude(longitude);
            point.setCoordinates(coordinates);
            writePoint(gen, point);
        }
        gen.writeEndObject();
    }

    private void writePoint(JsonGenerator gen, Point p) throws IOException {
        gen.writeStartObject();
      //  gen.writeStringField(GeoJSON.TYPE.getValue(), GeoJSON.POINT.getValue());
      //  gen.writeFieldName(GeoJSON.COORDINATES.getValue());
        writePointCoords(gen, p);
        gen.writeEndObject();
    }

    private void writePointCoords(JsonGenerator gen, Point p) throws IOException {
        gen.writeStartArray();
        gen.writeNumber(p.getCoordinates().getLongitude());
        gen.writeNumber(p.getCoordinates().getLatitude());
        gen.writeEndArray();
    }

}
