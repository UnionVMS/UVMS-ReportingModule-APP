/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.asset.gateway.ReportingAssetGateway;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;

@Stateless
public class AssetServiceBean {

    @Inject
    private ReportingAssetGateway reportingAssetGateway;


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Interceptors(SimpleTracingInterceptor.class)
    public Map<String, Asset> getAssetMap(final FilterProcessor processor) {
        Set<Asset> assetList = new HashSet<>();

            if (processor.hasAssets()) {
                assetList.addAll(reportingAssetGateway.getAssetListByQuery(processor.toAssetListQuery()));
            }
            if (processor.hasAssetGroups()) {
                assetList.addAll(reportingAssetGateway.getAssetGroup(processor.getAssetGroupList()));
            }

        return ExtAssetMessageMapper.getAssetMap(assetList);
    }

    public List<Asset> getAssets(AssetListQuery assetList){
            return reportingAssetGateway.getAssetListByQuery(assetList);

    }

    public List<Asset> getAssets(String moduleMessage, TextMessage response) throws AssetModelMapperException {
        return ExtAssetMessageMapper.mapToAssetListFromResponse(response, moduleMessage);
    }

}