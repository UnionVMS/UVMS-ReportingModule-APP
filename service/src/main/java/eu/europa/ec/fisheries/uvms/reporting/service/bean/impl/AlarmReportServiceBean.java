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
import java.time.Instant;
import java.util.Date;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AlarmReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AlarmRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AlarmReportServiceBean implements AlarmReportService {

    @Inject
    private AlarmRepository alarmRepository;

    @Override
    public void createOrUpdate(TicketType alarm) {
        Alarm alarmEntity = alarmRepository.findAlarmByGuid(alarm.getGuid());
        if (alarmEntity != null) {
            alarmRepository.updateAlarmEntity(mapToAlarmEntity(alarm));
        } else {
            alarmRepository.createAlarmEntity(mapToAlarmEntity(alarm));
        }
    }

    private eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm mapToAlarmEntity(TicketType inputData) {
        Alarm alarm = new Alarm();
        alarm.setGuid(inputData.getGuid());
        alarm.setAssetHistGuid(inputData.getAssetGuid());
        alarm.setMovementGuid(inputData.getMovementGuid());
        alarm.setOpenDate(parseDate(inputData.getOpenDate()));
        alarm.setUpdated(parseDate(inputData.getUpdated()));
        alarm.setUpdatedBy(inputData.getUpdatedBy());
        alarm.setRuleName(inputData.getRuleName());
        alarm.setStatus(inputData.getStatus().value());
        return alarm;
    }

    private Date parseDate(String date) {
        String formattedTextDate = date;
        try {
            formattedTextDate = formattedTextDate.replaceFirst(" ", "T");
            formattedTextDate = formattedTextDate.substring(0, formattedTextDate.indexOf(" "));
            formattedTextDate = formattedTextDate + "Z";
            Instant instant = Instant.parse(formattedTextDate);
            return Date.from(instant);
        } catch (Exception e) {
            log.error("An error occured when parsing date " + date + " for saving to alarm reporting db, will save null");
        }
        return null;
    }
}
