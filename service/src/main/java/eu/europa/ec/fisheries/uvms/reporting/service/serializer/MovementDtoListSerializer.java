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
import java.util.List;

/**
 * //TODO create test
 */
public class MovementDtoListSerializer extends JsonSerializer<List<MovementDto>> {

    private static final String FEATURE_COLLECTION = "FeatureCollection";
    private static final String FEATURE = "features";
    private static final String TYPE = "type";

    @Override
    public void serialize(List<MovementDto> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        gen.writeStringField(TYPE, FEATURE_COLLECTION);
        gen.writeFieldName(FEATURE);
        gen.writeObject(value);
        gen.writeEndObject();
    }


}
