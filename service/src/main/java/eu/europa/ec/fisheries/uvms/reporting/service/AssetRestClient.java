package eu.europa.ec.fisheries.uvms.reporting.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetListResponse;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetQuery;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Stateless
@Slf4j
public class AssetRestClient {

    private WebTarget webTarget;
    private ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .findAndRegisterModules();

    @Resource(name = "java:global/asset_endpoint")
    String assetUrl;

    @PostConstruct
    private void setUpClient() {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(assetUrl);
    }

    public List<AssetDTO> getAssetList(AssetQuery query) {
        String response = webTarget
                .path("/internal/query")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(query), String.class);

        try {
            AssetListResponse assetListResponse = mapper.readValue(response, AssetListResponse.class);
            return assetListResponse.getAssetList();
        } catch (IOException e) {
            log.error("Error when retrieving GetMovementMapByQueryResponse.", e);
            throw new RuntimeException(e);
        }
    }

    public List<AssetDTO> getAssetsByGroupIds(List<UUID> idList) {
        String response = webTarget
                .path("/internal/group/asset")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(idList), String.class);

        try {
            return Arrays.asList(mapper.readValue(response, AssetDTO[].class));
        } catch (IOException e) {
            log.error("Error when retrieving List<AssetDTO>.", e);
            throw new RuntimeException(e);
        }
    }
}
