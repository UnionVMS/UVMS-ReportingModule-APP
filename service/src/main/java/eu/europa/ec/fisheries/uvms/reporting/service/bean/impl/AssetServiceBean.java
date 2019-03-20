/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetListResponse;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetQuery;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetQueryMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

@LocalBean
@Stateless
public class AssetServiceBean {

    private WebTarget webTarget;
    private ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .findAndRegisterModules();

    @Resource(name = "java:global/asset_endpoint")
    private String assetUrl;

    @PostConstruct
    private void setUpClient() {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(assetUrl);
    }

    public Map<String, Asset> getAssetMap(final FilterProcessor processor) {

        Map<String, Asset> assetMap = new HashMap<>();
        List<Asset> assetList;

        if (processor.hasAssets()) {
            AssetListQuery assetListQuery = processor.toAssetListQuery();
            AssetQuery query = AssetQueryMapper.assetListQueryToAssetQuery(assetListQuery);

            List<AssetDTO> assetDTOList = getAssetList(query);
            assetList = AssetQueryMapper.dtoListToAssetList(assetDTOList);
            assetMap = AssetQueryMapper.assetListToAssetMap(assetList);
        }

        if (processor.hasAssetGroups()) {
            Set<AssetGroup> assetGroups = processor.getAssetGroupList();
            List<UUID> idList = AssetQueryMapper.getGroupListIds(assetGroups);

            List<AssetDTO> assetDTOList = getAssetsByGroupIds(idList);
            assetList = AssetQueryMapper.dtoListToAssetList(assetDTOList);
            assetMap = AssetQueryMapper.assetListToAssetMap(assetList);
        }
        return assetMap;
    }

    public List<Asset> getAssets(AssetListQuery assetListQuery) {
        AssetQuery query = AssetQueryMapper.assetListQueryToAssetQuery(assetListQuery);
        List<AssetDTO> list = getAssetList(query);
        return AssetQueryMapper.dtoListToAssetList(list);
    }

    private List<AssetDTO> getAssetList(AssetQuery query) {
        String response = webTarget
                .path("/internal/query")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(query), String.class);

        try {
            AssetListResponse assetListResponse = mapper.readValue(response, AssetListResponse.class);
            return assetListResponse.getAssetList();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when retrieving GetMovementMapByQueryResponse.", e);
        }
    }

    private List<AssetDTO> getAssetsByGroupIds(List<UUID> idList) {
        String response = webTarget
                .path("/internal/group/asset")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(idList), String.class);

        try {
            return Arrays.asList(mapper.readValue(response, AssetDTO[].class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when retrieving List<AssetDTO>.", e);
        }
    }
}
