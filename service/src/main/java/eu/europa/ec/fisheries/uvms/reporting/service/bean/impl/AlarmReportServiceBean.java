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

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AlarmReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AlarmRepository;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AlarmReportServiceBean implements AlarmReportService {

    @Inject
    private AlarmRepository alarmRepository;

    @Override
    public void createOrUpdate(TicketType alarm) {
//        eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset assetEntity = assetRepository.findAssetByAssetHistoryGuid(asset.getEventHistory().getEventId());
//        if (assetEntity != null) {
//            assetEntity.setAssetGuid(asset.getAssetId().getGuid());
//            assetEntity.setCfr(asset.getCfr());
//            assetEntity.setIrcs(asset.getIrcs());
//            assetEntity.setIccat(asset.getIccat());
//            assetEntity.setUvi(asset.getUvi());
//            assetEntity.setGfcm(asset.getGfcm());
//            assetEntity.setExternalMarking(asset.getExternalMarking());
//            assetEntity.setName(asset.getName());
//            assetEntity.setLengthOverall(Optional.ofNullable(asset.getLengthOverAll()).map(BigDecimal::doubleValue).orElse(null));
//            assetEntity.setMainGearType(asset.getGearType());
//            assetEntity.setCountryCode(asset.getCountryCode());
//            assetEntity.setAssetHistActive(asset.isActive());
//        } else {
//            assetRepository.createAssetEntity(mapToAssetEntity(asset));
//        }
//
//        assetRepository.makeOtherHistoryEntriesOfAssetInactiveExceptCurrentHistId(asset.getAssetId().getGuid(), asset.getEventHistory().getEventId());

//        if (!asset.isActive()) { // archiving asset event
//            int affected = assetRepository.updateHistoryRecordsAsInactiveForAssetGuid(asset.getAssetId().getGuid());
//        }
        alarmRepository.createAlarmEntity(mapToAlarmEntity(alarm));
    }

    private eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm mapToAlarmEntity(TicketType inputData) {
        eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm alarm = new eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm();

        return alarm;
    }
}
