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

import eu.europa.ec.fisheries.uvms.reporting.service.bean.AlarmRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.AlarmDao;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AlarmRepositoryBean implements AlarmRepository {

    @Inject
    private AlarmDao alarmDao;

    @Override
    public Alarm createAlarmEntity(Alarm entity) {
        return alarmDao.createEntity(entity);
    }

    @Override
    public Alarm findAlarmByGuid(String guid) {
        return alarmDao.findByGuid(guid);
    }

    @Override
    public Alarm updateAlarmEntity(Alarm entity) {
        return alarmDao.update(entity);
    }

}