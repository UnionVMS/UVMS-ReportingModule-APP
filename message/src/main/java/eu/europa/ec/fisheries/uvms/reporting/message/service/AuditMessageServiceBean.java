package eu.europa.ec.fisheries.uvms.reporting.message.service;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;
import static eu.europa.ec.fisheries.uvms.reporting.model.Constants.*;

/**
 * AuditMessageServiceBean responsible to send an receive messages to and from AUDIT module from Reporting
 * The implementation of sending message to the queue is handled in generic implementation of AbstractProducer
 * 
 * @see {@link AbstractMessageService}
 *
 */
@Stateless
@LocalBean
public class AuditMessageServiceBean extends AbstractMessageService {

	@Resource(mappedName = QUEUE_AUDIT_EVENT)
    private Destination request;

    @Resource(mappedName = QUEUE_AUDIT)
    private Destination response;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Override
    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    protected Destination getEventDestination() {
        return request;
    }

    @Override
    protected Destination getResponseDestination() {
        return response;
    }

    @Override
    protected String getModuleName() {
        return MODULE_NAME;
    }

}
