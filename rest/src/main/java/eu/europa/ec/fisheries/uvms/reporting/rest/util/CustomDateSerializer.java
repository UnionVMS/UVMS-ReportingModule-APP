package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;




import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;



public class CustomDateSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Date> {
	 
	private static SimpleDateFormat formatter = new SimpleDateFormat(RestConstants.DATE_TIME_UI_FORMAT);
		 
    @Override
    public void serialize (Date value, JsonGenerator gen, SerializerProvider arg2)
      throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}
