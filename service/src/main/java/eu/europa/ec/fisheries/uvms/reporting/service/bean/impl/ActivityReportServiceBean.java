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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

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
                String purposeCode = activityData.getFLUXReportDocument().getPurposeCode().getValue();
                crateFishingActivity(fa, purposeCode);
                createFishingTrip(fa.getSpecifiedFishingTrip());
            }
        }

    }

    private void createFishingTrip(FishingTrip fishingTrip) {
        Trip trip = new Trip();

        mapTripId(fishingTrip, trip);
        activityRepository.createTripEntity(trip);
    }

    private void mapTripId(FishingTrip fishingTrip, Trip trip) {
        for (IDType id : fishingTrip.getIDS()) {
            trip.setTripIdScheme(id.getSchemeID());
            trip.setTripId(id.getValue());
        }
    }

    private void crateFishingActivity(FishingActivity fishingActivity, String purposeCode) {
        Activity activity = new Activity();
        mapFishingGear(fishingActivity, activity);
        activity.setPurposeCode(purposeCode);

        mapSpecies(fishingActivity, activity);

        mapAreas(fishingActivity, activity);

        // todo map all data

        activityRepository.createActivityEntity(activity);
    }

    private void mapAreas(FishingActivity fishingActivity, Activity activity) {
        List<Area> areas = new ArrayList<>();
        for (FLUXLocation relatedFLUXLocation : fishingActivity.getRelatedFLUXLocations()) {
            Area a = activityRepository.findAreaByTypeCodeAndAreaCode(relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode().getListID(), relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode().getValue());
            a = createAreaIfNotExists(relatedFLUXLocation, a);
            areas.add(a);
        }
        activity.setAreas(areas);
    }

    private Area createAreaIfNotExists(FLUXLocation relatedFLUXLocation, Area a) {
        if (a == null) {
            a = new Area();
            a.setAreaTypeCode(relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode().getListID());
            a.setAreaCode(relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode().getValue());
            activityRepository.createArea(a);
        }
        return a;
    }

    private void mapSpecies(FishingActivity fishingActivity, Activity activity) {
        Set<String> speciesCodes = new HashSet<>();
        for (FACatch faCatch : fishingActivity.getSpecifiedFACatches()) {
            speciesCodes.add(faCatch.getSpeciesCode().getValue());
        }
        activity.setSpecies(speciesCodes);
    }

    private void mapFishingGear(FishingActivity fishingActivity, Activity activity) {
        Set<String> gearTypes = new HashSet<>();
        for (FishingGear g : fishingActivity.getSpecifiedFishingGears()) {
            gearTypes.add(g.getTypeCode().getValue());
        }
        activity.setGears(gearTypes);
    }

}
