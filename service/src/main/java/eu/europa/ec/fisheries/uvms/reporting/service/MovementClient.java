package eu.europa.ec.fisheries.uvms.reporting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.schema.movement.module.v1.GetMovementMapByQueryResponse;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Stateless
@Slf4j
public class MovementClient {

    private static final String BASE_URL = "http://localhost:8080/unionvms/";
    private WebTarget webTarget;
    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void setUpClient() {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URL);
    }

    public GetMovementMapByQueryResponse getMovementMapResponseTypes(MovementQuery query) {
        String response = webTarget
                .path("movement/rest/internal/")
                .path("movementMapByQuery")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(query), String.class);

        try {
            return mapper.readValue(response, GetMovementMapByQueryResponse.class);
        } catch (IOException e) {
            log.error("Error when retrieving GetMovementMapByQueryResponse.", e);
            throw new RuntimeException(e);
        }
    }
}
