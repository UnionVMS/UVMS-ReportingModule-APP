/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.AssetModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@LocalBean
@Stateless
public class AssetServiceBean {

    @EJB
    private AssetModuleSenderBean assetSender;

    @EJB
    private ReportingModuleReceiverBean receiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Set<Asset> getAssetMap(final FilterProcessor processor) throws ReportingServiceException {
        Set<Asset> assetList = new HashSet<>();

        try {
            if (processor.hasAssets()) {
                String request = ExtAssetMessageMapper.createAssetListModuleRequest(processor.toAssetListQuery());
                String moduleMessage = assetSender.sendModuleMessage(request, receiver.getDestination());
                TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
                List<Asset> assets = getAssets(moduleMessage, response);
                assetList.addAll(assets);
            }

            if (processor.hasAssetGroups()) {
                String request = ExtAssetMessageMapper.createAssetListModuleRequest(processor.getAssetGroupList());
                String moduleMessage = assetSender.sendModuleMessage(request, receiver.getDestination());
                TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
                List<Asset> groupList = getAssets(moduleMessage, response);
                assetList.addAll(groupList);
            }

        } catch (MessageException | AssetModelMapperException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM ASSET", e);
        }

        return assetList;
    }

    public List<Asset> getAssets(AssetListQuery assetList) throws ReportingServiceException {
        try {
            String request = AssetModuleRequestMapper.createAssetListModuleRequest(assetList);
            String moduleMessage = assetSender.sendModuleMessage(request, receiver.getDestination());
            TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
            return getAssets(moduleMessage, response);
        } catch (AssetModelMapperException | MessageException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM ASSET", e);
        }
    }

    // UT
    protected List<Asset> getAssets(String moduleMessage, TextMessage response) throws AssetModelMapperException {
        return ExtAssetMessageMapper.mapToAssetListFromResponse(response, moduleMessage);
    }
}