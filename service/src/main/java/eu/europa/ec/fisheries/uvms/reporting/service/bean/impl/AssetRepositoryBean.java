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

import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.AssetDao;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AssetRepositoryBean implements AssetRepository {

    @Inject
    private AssetDao assetDao;

    @Override
    public Asset createAssetEntity(Asset entity) {
        return assetDao.createEntity(entity);
    }

    @Override
    public Asset findAssetByAssetHistoryGuid(String guid) {
        return assetDao.findAssetByAssetHistoryGuid(guid);
    }

    @Override
    public int updateHistoryRecordsAsInactiveForAssetGuid(String guid) {
        return assetDao.updateHistoryRecordsAsInactiveForAssetGuid(guid);
    }

    @Override
    public int makeOtherHistoryEntriesOfAssetInactiveExceptCurrentHistId(String guid, String eventId) {
        return assetDao.makeOtherHistoryEntriesOfAssetInactiveExceptCurrentHistId(guid, eventId);
    }
}
