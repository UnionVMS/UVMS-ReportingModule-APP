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
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Catch;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CatchLocation;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CatchProcessing;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Location;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;
import lombok.Builder;
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
    public Catch createCatchEntity(Catch speciesCatch) {
        return activityDao.createEntity(speciesCatch);
    }


    @Override
    public Trip createTripEntity(Trip entity) {
        return activityDao.createEntity(entity);
    }

    @Override
    public Location createLocation(Location entity) {
        return activityDao.createEntity(entity);
    }

    @Override
    public Location findLocationByTypeCodeAndCode(String typeCode, String code) {
        return activityDao.findLocationByTypeCodeAndCode(typeCode, code);
    }

    @Override
    public void updateOlderReportsAsNotLatest(String faReportId, Long latestActivityId) {
        activityDao.updateOlderReportsAsNotLatest(faReportId, latestActivityId);
    }

    @Override
    public CatchLocation createActivityCatchLocation(CatchLocation loc) {
        return activityDao.createEntity(loc);
    }

    @Override
    public CatchProcessing createActivityCatchProcessing(CatchProcessing processing){
        return activityDao.createEntity(processing);
    }

}
