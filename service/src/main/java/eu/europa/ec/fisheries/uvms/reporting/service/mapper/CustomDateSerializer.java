package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.common.DateUtils;

import java.io.IOException;
import java.util.Date;

// TODO create unit test
class CustomDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize (Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
        gen.writeString(DateUtils.UI_FORMATTER.print(value.getTime()));
    }
}
