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
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityAreas;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ForwardReportToSubscriptionRequest;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ReportToSubscription;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Catch;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
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

    @Inject
    private AssetRepository assetRepository;

    private final WKTReader wktReader = new WKTReader();

    @Override
    @Transactional
    public void processReports(ForwardReportToSubscriptionRequest activityData) {
        activityData.getFaReports().forEach(this::createActivitiesAndTripsFromReport);
    }

    private void createActivitiesAndTripsFromReport(ReportToSubscription reports) {
        List<Asset> assets = new ArrayList<>();
        reports.getAssetHistoryGuids().forEach(assetHistGuid -> {
            Optional.ofNullable(assetRepository.findAssetByAssetHistoryGuid(assetHistGuid)).ifPresent(assets::add);
        });
        for (int i = 0; i < reports.getFishingActivities().size(); i++) {
            createFishingActivityAndTrip(reports.getFishingActivities().get(i),
                    reports.getFluxFaReportMessageIds().get(i).getId(),
                    reports.getActivitiesWktLists().get(i),
                    reports.getActivityAreas().get(i),
                    reports.getFaReportType(),
                    assets);
        }
    }

    private void createFishingActivityAndTrip(FishingActivity fishingActivity, String reportId, String wkt, ActivityAreas activityAreas, String reportType, List<Asset> assets) {
        Activity activity = new Activity();
//        activity.setPurposeCode();
        activity.setActivityType(fishingActivity.getTypeCode().getValue());
        activity.setAcceptedDate(Date.from(fishingActivity.getOccurrenceDateTime().getDateTime().toGregorianCalendar().toInstant()));
        activity.setFaReportId(reportId);
        activity.setReportType(reportType);
        try {
            MultiPoint geometry = (MultiPoint) wktReader.read(wkt);
            geometry.setSRID(4326);
            activity.setActivityCoordinates(geometry);
        } catch (ParseException e) {
            log.error("unable to set geometry from wkt string: {}", wkt);
        }
        mapFishingGear(fishingActivity, activity);
        mapAreas(fishingActivity, activity);

        activity.setAsset(assets.stream().findFirst().orElse(null));

        activity = activityRepository.createActivityEntity(activity);
        mapSpecies(fishingActivity, activity);

        Trip trip = createFishingTrip(fishingActivity.getSpecifiedFishingTrip(), assets);
        activity.setTripId(trip.getTripId());

    }

    private void mapAreas(FishingActivity fishingActivity, Activity activity) {
        Set<Area> areas = new HashSet<>();
        for (FLUXLocation relatedFLUXLocation : fishingActivity.getRelatedFLUXLocations()) {
            Area a = activityRepository.findAreaByTypeCodeAndAreaCode(relatedFLUXLocation.getTypeCode().getValue(), relatedFLUXLocation.getID().getValue());
            a = createAreaIfNotExists(relatedFLUXLocation, a);
            areas.add(a);
        }
        activity.setAreas(areas);
    }

    private Area createAreaIfNotExists(FLUXLocation relatedFLUXLocation, Area a) {
        if (a == null) {
            a = new Area();
            a.setAreaTypeCode(relatedFLUXLocation.getTypeCode().getValue());
            a.setAreaCode(relatedFLUXLocation.getID().getValue());
            activityRepository.createArea(a);
        }
        return a;
    }

    private void mapSpecies(FishingActivity fishingActivity, Activity activity) {
        List<Catch> catches = new ArrayList<>();
        for (FACatch faCatch : fishingActivity.getSpecifiedFACatches()) {
            Catch speciesCatch = new Catch();
            speciesCatch.setSpeciesCode(faCatch.getSpeciesCode().getValue());
            speciesCatch.setWeightMeasureUnitCode(faCatch.getWeightMeasure().getUnitCode());
            speciesCatch.setWeightMeasure(faCatch.getWeightMeasure().getValue().doubleValue());

            speciesCatch.setActivityId(activity.getId());
            activityRepository.createCatchEntity(speciesCatch);

            catches.add(speciesCatch);
        }
        activity.setSpeciesCatch(catches);
    }

    private void mapFishingGear(FishingActivity fishingActivity, Activity activity) {
        Set<String> gearTypes = new HashSet<>();
        for (FishingGear g : fishingActivity.getSpecifiedFishingGears()) {
            gearTypes.add(g.getTypeCode().getValue());
        }
        activity.setGears(gearTypes);
    }

    private Trip createFishingTrip(FishingTrip fishingTrip, List<Asset> assets) {
        Trip trip = new Trip();
        mapTripId(fishingTrip, trip);
        trip.setAsset(assets.stream().findFirst().orElse(null));
        activityRepository.createTripEntity(trip);
        return trip;
    }

    private void mapTripId(FishingTrip fishingTrip, Trip trip) {
        for (IDType id : fishingTrip.getIDS()) {
            trip.setTripIdScheme(id.getSchemeID());
            trip.setTripId(id.getValue());
        }
    }
}
