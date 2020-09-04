/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetRepository;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AssetReportServiceBean implements AssetReportService {

    @Inject
    private AssetRepository assetRepository;

    @Override
    public void createOrUpdate(Asset asset) {
        eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset assetEntity = assetRepository.findAssetByAssetHistoryGuid(asset.getEventHistory().getEventId());
        if (assetEntity != null) {
            assetEntity.setAssetGuid(asset.getAssetId().getGuid());
            assetEntity.setCfr(asset.getCfr());
            assetEntity.setIrcs(asset.getIrcs());
            assetEntity.setIccat(asset.getIccat());
            assetEntity.setUvi(asset.getUvi());
            assetEntity.setGfcm(asset.getGfcm());
            assetEntity.setExternalMarking(asset.getExternalMarking());
            assetEntity.setName(asset.getName());
            assetEntity.setLengthOverall(Optional.ofNullable(asset.getLengthOverAll()).map(BigDecimal::doubleValue).orElse(null));
            assetEntity.setMainGearType(asset.getGearType());
            assetEntity.setCountryCode(asset.getCountryCode());
            assetEntity.setAssetHistActive(asset.isActive());
        } else {
            assetRepository.createAssetEntity(mapToAssetEntity(asset));
        }

        if (asset.isActive()) {
            assetRepository.makeOtherHistoryEntriesOfAssetInactiveExceptCurrentHistId(asset.getAssetId().getGuid(), asset.getEventHistory().getEventId());
        }
//        if (!asset.isActive()) { // archiving asset event
//            int affected = assetRepository.updateHistoryRecordsAsInactiveForAssetGuid(asset.getAssetId().getGuid());
//        }
    }

    private eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset mapToAssetEntity(Asset a) {
        eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset asset = new eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset();
        asset.setCfr(a.getCfr());
        asset.setIrcs(a.getIrcs());
        asset.setIccat(a.getIccat());
        asset.setUvi(a.getUvi());
        asset.setGfcm(a.getGfcm());
        asset.setExternalMarking(a.getExternalMarking());
        asset.setName(a.getName());
        asset.setCountryCode(a.getCountryCode());
        asset.setMainGearType(a.getGearType());
        asset.setLengthOverall(a.getLengthOverAll().doubleValue());
        asset.setAssetGuid(a.getAssetId().getGuid());
        asset.setAssetHistGuid(a.getEventHistory().getEventId());
        asset.setAssetHistActive(a.isActive());
        return asset;
    }
}
