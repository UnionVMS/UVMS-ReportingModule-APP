package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.RulesProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesModuleResponseMapper;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.List;

/**
 * Created by padhyad on 3/23/2016.
 */
@Stateless
@Local(RulesEventService.class)
@Slf4j
public class RulesEventServiceBean implements RulesEventService {

    @EJB
    private RulesProducerBean producer;

    @EJB
    private ReportingModuleReceiverBean consumer;

    @Override
    public List<TicketAndRuleType> GetAlarmsForMovements(List<String> movementId) throws ReportingServiceException {
        try {
            String request = RulesModuleRequestMapper.createGetTicketsAndRulesByMovementsRequest(movementId);
            String correlationId = producer.sendModuleMessage(request, consumer.getDestination());
            Message message = consumer.getMessage(correlationId, TextMessage.class);
            GetTicketsAndRulesByMovementsResponse response = RulesModuleResponseMapper.mapToGetTicketsAndRulesByMovementsFromResponse(getText(message));
            if (response == null) {
                return null;
            }
            return response.getTicketsAndRules();
        } catch (RulesModelMarshallException | MessageException | JMSException e) {
            throw new ReportingServiceException(e);
        }
    }

    private TextMessage getText(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            return (TextMessage) message;
        }
        return null;
    }
}
