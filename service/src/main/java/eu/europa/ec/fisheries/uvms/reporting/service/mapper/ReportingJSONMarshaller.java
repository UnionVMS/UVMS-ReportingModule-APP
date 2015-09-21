package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelMarshallException;
import eu.europa.ec.fisheries.uvms.rest.mapper.AbstractJSONMarshaller;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ReportingJSONMarshaller extends AbstractJSONMarshaller {

    /**
     * Convert Json string back to Java object.
     *
     * @param json The Json string to convert from
     * @param clazz The java class to convert to
     * @throws IOException
     */
    public <T> T marshall(String json, Class clazz) throws ReportingModelMarshallException {
        try {
            return this.marshallJsonStringToObject(json, clazz);
        } catch (IOException e) {
            log.error("[ Error when marshalling data. ] {}", e.getMessage());
            throw new ReportingModelMarshallException("Error when marshalling " + json + " to " + clazz.getName());
        }
    }
}