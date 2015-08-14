package eu.europa.ec.fisheries.uvms.reporting.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import org.apache.commons.lang3.StringUtils;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import java.io.IOException;

public class MovementDtoSerializer extends JsonSerializer<MovementDto> {

    private static final String GEOMETRY = "geometry";
    private static final String PROPERTIES = "properties";
    private static final String FEATURE = "Feature";
    protected static final String TYPE = "type";
    protected static final String COORDINATES = "coordinates";
    protected static final String POINT = "Point";

    @Override
    public void serialize(MovementDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        MovementPoint position = value.getPosition();

        gen.writeStartObject();
        gen.writeStringField(TYPE, FEATURE);
        gen.writeFieldName(GEOMETRY);

        if (position != null){
            double latitude = position.getLatitude();
            double longitude = position.getLongitude();
            Point point = new Point();
            LngLatAlt coordinates = new LngLatAlt();
            coordinates.setLatitude(latitude);
            coordinates.setLongitude(longitude);
            point.setCoordinates(coordinates);
            writePoint(gen, point);
        }

        writeProperties(gen, value);
        gen.writeEndObject();
    }

    private void writeProperties(JsonGenerator gen, MovementDto value) throws IOException{
        gen.writeFieldName(PROPERTIES);
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("connectID", value.getConnectId());
        gen.writeStringField("status", value.getStatus());
        gen.writeNumberField("calculatedSpeed", value.getCalculatedSpeed());
        gen.writeNumberField("course", value.getCourse());
        gen.writeNumberField("measuredSpeed", value.getMeasuredSpeed());
        gen.writeStringField("messageType", value.getMessageType().value());
        if (value.getPositionTime() != null){
            String s = value.getPositionTime().toString();
            if (StringUtils.isNotBlank(s)){
                gen.writeStringField("positionTime", s);
            }
        }
        gen.writeObjectField("asset", value.getAsset());
        gen.writeEndObject();
    }

    private void writePoint(JsonGenerator gen, Point p) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(TYPE, POINT);
        gen.writeFieldName(COORDINATES);
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
