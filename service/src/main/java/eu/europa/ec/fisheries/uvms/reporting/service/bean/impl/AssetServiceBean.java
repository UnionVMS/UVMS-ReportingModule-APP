/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.uvms.asset.client.AssetClient;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetQuery;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetQueryMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.TextMessage;
import java.util.*;

@LocalBean
@Stateless
public class AssetServiceBean {

    @EJB
    private AssetClient assetClient;

    @Interceptors(SimpleTracingInterceptor.class)
    public Map<String, Asset> getAssetMap(final FilterProcessor processor) {

        Map<String, Asset> assetMap = new HashMap<>();
        List<Asset> assetList;

        if (processor.hasAssets()) {
            AssetListQuery assetListQuery = processor.toAssetListQuery();
            AssetQuery query = AssetQueryMapper.assetListQueryToAssetQuery(assetListQuery);

            List<AssetDTO> assetDTOList = assetClient.getAssetList(query);
            assetList = AssetQueryMapper.dtoListToAssetList(assetDTOList);
            assetMap = AssetQueryMapper.assetListToAssetMap(assetList);
        }
        if (processor.hasAssetGroups()) {

            Set<AssetGroup> assetGroups = processor.getAssetGroupList();
            List<UUID> idList = AssetQueryMapper.getGroupListIds(assetGroups);
            List<AssetDTO> assetDTOList = assetClient.getAssetsByGroupIds(idList);
            assetList = AssetQueryMapper.dtoListToAssetList(assetDTOList);
            assetMap = AssetQueryMapper.assetListToAssetMap(assetList);
        }
        return assetMap;
    }

    public List<Asset> getAssets(AssetListQuery assetListQuery) {
        AssetQuery query = AssetQueryMapper.assetListQueryToAssetQuery(assetListQuery);
        List<AssetDTO> list = assetClient.getAssetList(query);
        return AssetQueryMapper.dtoListToAssetList(list);
    }

    // UT
    public List<Asset> getAssets(String moduleMessage, TextMessage response) throws AssetModelMapperException {
        return ExtAssetMessageMapper.mapToAssetListFromResponse(response, moduleMessage);
    }

}