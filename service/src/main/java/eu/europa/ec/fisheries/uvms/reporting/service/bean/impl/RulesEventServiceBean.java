/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.schema.movementrules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.movementrules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.RulesProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.RulesEventService;
import eu.europa.ec.fisheries.uvms.movementrules.model.exception.MovementRulesFaultException;
import eu.europa.ec.fisheries.uvms.movementrules.model.exception.MovementRulesModelMapperException;
import eu.europa.ec.fisheries.uvms.movementrules.model.mapper.MovementRulesModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movementrules.model.mapper.MovementRulesModuleResponseMapper;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

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
            String request = MovementRulesModuleRequestMapper.createGetTicketsAndRulesByMovementsRequest(movementId);
            String correlationId = producer.sendModuleMessage(request, consumer.getDestination());
            Message message = consumer.getMessage(correlationId, TextMessage.class);
            GetTicketsAndRulesByMovementsResponse response = MovementRulesModuleResponseMapper.mapToGetTicketsAndRulesByMovementsFromResponse((TextMessage) message);
            return response != null ? response.getTicketsAndRules() : null;
        } catch (MessageException | JMSException | MovementRulesModelMapperException | MovementRulesFaultException e) {
            throw new ReportingServiceException(e);
        }
    }

}