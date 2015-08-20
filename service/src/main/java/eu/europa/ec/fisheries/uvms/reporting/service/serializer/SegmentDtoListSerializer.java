package eu.europa.ec.fisheries.uvms.reporting.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;

import java.io.IOException;
import java.util.List;

/**
 * //TODO create test
 */
public class SegmentDtoListSerializer extends JsonSerializer<List<SegmentDto>> {

    @Override
    public void serialize(List<SegmentDto> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
       // gen.writeStringField(GeoJSON.TYPE.getValue(), GeoJSON.FEATURE_COLLECTION.getValue());
       // gen.writeFieldName(GeoJSON.FEATURES.getValue());
        gen.writeObject(value);
        gen.writeEndObject();
    }


}
