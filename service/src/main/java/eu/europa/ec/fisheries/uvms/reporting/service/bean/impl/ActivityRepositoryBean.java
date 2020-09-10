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

import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ActivityDao;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ActivityRepositoryBean implements ActivityRepository {

    @Inject
    private ActivityDao activityDao;

    @Override
    public Activity createActivityEntity(Activity entity) {
        return activityDao.createEntity(entity);
    }

    @Override
    public Trip createTripEntity(Trip trip) {
        return activityDao.createEntity(trip);
    }
}
