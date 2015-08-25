package eu.europa.ec.fisheries.uvms.reporting.rest.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;





import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;




public class VisibilitySerializer extends com.fasterxml.jackson.databind.JsonSerializer<VisibilityEnum> {
		 
    @Override
    public void serialize (VisibilityEnum value, JsonGenerator gen, SerializerProvider arg2)
      throws IOException, JsonProcessingException {
    	switch (value) {
			case PRIVATE:gen.writeString("p"); break;
			case GLOBAL	:gen.writeString("g"); break;
			case SCOPE	:gen.writeString("c"); break;
		default:
			break;
		}
    }
}
