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
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;

@ApplicationScoped
@Slf4j
public class ActivityReportServiceBean implements ActivityReportService {

    @Inject
    private ActivityRepository activityRepository;

    @Override
    @Transactional
    public void createActivitiesAndTrips(FLUXFAReportMessage activityData) {
        for (FAReportDocument doc : activityData.getFAReportDocuments()) {
            for (FishingActivity fa : doc.getSpecifiedFishingActivities()) {
                crateFishingActivity(fa);
                createFishingTrip(fa.getSpecifiedFishingTrip());
            }
        }

    }

    private void createFishingTrip(FishingTrip fishingTrip) {
        Trip trip = new Trip();

        activityRepository.createTripEntity(trip);
    }

    private void crateFishingActivity(FishingActivity fishingActivity) {
        Activity activity = new Activity();
        Set<String> gearTypes = new HashSet<>();
        for (FishingGear g : fishingActivity.getSpecifiedFishingGears()) {
            gearTypes.add(g.getTypeCode().getValue());
        }
        activity.setGears(gearTypes);

        // todo map all data

        activityRepository.createActivityEntity(activity);
    }

}
