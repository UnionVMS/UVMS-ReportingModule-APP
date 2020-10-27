/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean.asset.gateway.impl;


import eu.europa.ec.fisheries.uvms.asset.rest.client.AssetClient;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.asset.gateway.ReportingAssetGateway;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import eu.europa.ec.fisheries.wsdl.asset.types.ListAssetResponse;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class ReportingAssetGatewayImpl implements ReportingAssetGateway {

    @Inject
    private AssetClient assetClient;

    @Override
    public List<Asset> getAssetListByQuery(AssetListQuery query) {
        try {
            ListAssetResponse assetListByQuery = assetClient.getAssetListByQuery(query);
            return assetListByQuery == null ? new ArrayList<>():assetListByQuery.getAsset();
        } catch (Exception e) {
            log.error("Error in communication with asset: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Asset> getAssetGroup(Set<AssetGroup> assetGroupQuery) {
        try {

           List<AssetGroup> assetGroupQueryList = assetGroupQuery == null? null: new ArrayList<>(assetGroupQuery);
           return assetClient.getAssetGroup(assetGroupQueryList);
        } catch (Exception e) {
            log.error("Error in communication with asset: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
