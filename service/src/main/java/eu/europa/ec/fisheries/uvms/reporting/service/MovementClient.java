package eu.europa.ec.fisheries.uvms.reporting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.schema.movement.module.v1.GetMovementMapByQueryResponse;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.rest.security.InternalRestTokenHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Stateless
@Slf4j
public class MovementClient {

    private WebTarget webTarget;
    private ObjectMapper mapper = new ObjectMapper();

    @Resource(name = "java:global/movement_endpoint")
    String movementUrl;

    @EJB
    private InternalRestTokenHandler tokenHandler;

    @PostConstruct
    private void setUpClient() {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(movementUrl);
    }

    public GetMovementMapByQueryResponse getMovementMapResponseTypes(MovementQuery query) {
        String response = webTarget
                .path("internal/")
                .path("movementMapByQuery")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, tokenHandler.createAndFetchToken("user"))
                .post(Entity.json(query), String.class);

        try {
            return mapper.readValue(response, GetMovementMapByQueryResponse.class);
        } catch (IOException e) {
            log.error("Error when retrieving GetMovementMapByQueryResponse.", e);
            throw new RuntimeException(e);
        }
    }
}
