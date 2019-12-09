package eu.europa.ec.fisheries.uvms.reporting.service.domain.dto;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.format.DateTimeFormatter;

public class IncidentInstantDeserializer  extends InstantDeserializer {
    public IncidentInstantDeserializer() {
        super(InstantDeserializer.INSTANT, DateTimeFormatter.ISO_INSTANT);
    }
}
