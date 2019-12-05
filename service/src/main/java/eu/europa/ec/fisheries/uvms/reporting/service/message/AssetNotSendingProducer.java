package eu.europa.ec.fisheries.uvms.reporting.service.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.IncidentCreate;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.interfaces.IncidentUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.jms.*;

public class AssetNotSendingProducer {

    private final static Logger LOG = LoggerFactory.getLogger(AssetNotSendingProducer.class);

    @Resource(mappedName = "java:/" + MessageConstants.EVENT_STREAM_TOPIC)
    private Destination destination;

    @Inject
    @JMSConnectionFactory("java:/ConnectionFactory")
    JMSContext context;

    private ObjectMapper om = new ObjectMapper();

    @PostConstruct
    public void init() {
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public void incidentCreated(@Observes(during = TransactionPhase.AFTER_SUCCESS) @IncidentCreate Incident incident) {
        try {
            sendEvent(incident, "Incident");
        } catch (Exception e){
            LOG.error("Error while posting incident to queue: ", e);
            throw new RuntimeException(e);
        }
    }

    public void incidentUpdated(@Observes(during = TransactionPhase.AFTER_SUCCESS) @IncidentUpdate Incident incident) {
        try {
            sendEvent(incident, "IncidentUpdate");
        } catch (Exception e){
            LOG.error("Error while posting incident to queue: ", e);
            throw new RuntimeException(e);
        }
    }


    private void sendEvent(Incident incident, String eventName) {
        try {
            String  outgoingJson =   om.writeValueAsString(incident);
            TextMessage message = this.context.createTextMessage(outgoingJson);
            message.setStringProperty(MessageConstants.EVENT_STREAM_EVENT, eventName);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
            context.createProducer().setDeliveryMode(1).setTimeToLive(5000L).send(destination, message);
        } catch (JMSException | JsonProcessingException e) {
            LOG.error("Error while sending ticket event to event stream topic: ", e);
            throw new RuntimeException(e);
        }
    }





}
