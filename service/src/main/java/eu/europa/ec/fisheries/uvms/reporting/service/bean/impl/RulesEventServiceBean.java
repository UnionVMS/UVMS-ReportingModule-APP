/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.europa.ec.fisheries.schema.movementrules.module.v1.GetTicketsAndRulesByMovementsRequest;
import eu.europa.ec.fisheries.schema.movementrules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.movementrules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.RulesEventService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import java.util.List;

@Stateless
@Local(RulesEventService.class)
@Slf4j
public class RulesEventServiceBean implements RulesEventService {

    @Resource(name = "java:global/movement-rules_endpoint")
    private String movementRulesEndpoint;

    private Client client;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();

        client.register(new ContextResolver<ObjectMapper>() {
            @Override
            public ObjectMapper getContext(Class<?> type) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JaxbAnnotationModule());
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return mapper;
            }
        });
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    @Override
    public List<TicketAndRuleType> GetAlarmsForMovements(List<String> movementId) {

        GetTicketsAndRulesByMovementsRequest request = new GetTicketsAndRulesByMovementsRequest();
        request.getMovementGuids().addAll(movementId);

        WebTarget target = client.target(movementRulesEndpoint + "/internal");

        GetTicketsAndRulesByMovementsResponse response = target
                .path("tickets-and-rules-by-movement")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request), GetTicketsAndRulesByMovementsResponse.class);

        return response != null ? response.getTicketsAndRules() : null;
    }
}
