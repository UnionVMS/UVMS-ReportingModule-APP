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
import javax.jms.TextMessage;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityAreas;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ForwardReportToSubscriptionRequest;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ReportToSubscription;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ActivityModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.util.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityReportService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Activity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Catch;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CatchLocation;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Location;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Trip;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByCodeResponse;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@ApplicationScoped
@Slf4j
public class ActivityReportServiceBean implements ActivityReportService {

    public static final String REPORT_TYPE_DECLARATION = "DECLARATION";
    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private AssetRepository assetRepository;

    @Inject
    private ActivityModuleSenderBean activityModuleSenderBean;

    @Inject
    private ReportingModuleReceiverBean reportingModuleReceiverBean;

    @Inject
    private SpatialProducerBean spatialProducerBean;

    private final WKTReader wktReader = new WKTReader();

    @Override
    @Transactional
    public void processReports(ForwardReportToSubscriptionRequest activityData) {
        activityData.getFaReports().forEach(this::processReport);
    }

    private void processReport(ReportToSubscription reports) {
        List<Asset> assets = new ArrayList<>();
        reports.getAssetHistoryGuids().forEach(assetHistGuid -> {
            Optional.ofNullable(assetRepository.findAssetByAssetHistoryGuid(assetHistGuid)).ifPresent(assets::add);
        });
        for (int i = 0; i < reports.getFishingActivities().size(); i++) {
            createFishingActivityAndTrip(reports.getFishingActivities().get(i),
                    reports.getFluxFaReportMessageIds().get(i).getId(),
                    reports.getActivitiesWktLists() != null && reports.getActivitiesWktLists().size() > 0 ? reports.getActivitiesWktLists().get(i) : null,
                    reports.getActivityAreas().get(i),
                    reports.getFaReportType(),
                    assets);
        }
    }

    private void createFishingActivityAndTrip(FishingActivity fishingActivity, String reportId, String wkt, ActivityAreas activityAreas, String reportType, List<Asset> assets) {
        Activity activity = new Activity();

        mapBasicActivityFields(fishingActivity, reportId, wkt, reportType, activity);
        mapFishingGear(fishingActivity, activity);
        mapActivityLocations(fishingActivity, activity);
        mapAsset(assets, activity);

        activity = activityRepository.createActivityEntity(activity);

        mapSpecies(fishingActivity, activity, assets);

        Optional<IDType> tripId = fishingActivity.getSpecifiedFishingTrip().getIDS().stream().findFirst();
        if (tripId.isPresent()) {
            FishingTripResponse tripDetailsFromActivity = getTripDetailsFromActivity(tripId.get().getValue());
            Trip trip = createFishingTrip(fishingActivity.getSpecifiedFishingTrip(), assets, tripDetailsFromActivity);
            activity.setTripId(trip.getTripId());

            Optional<FishingActivitySummary> fishingActivitySummaryOptional = tripDetailsFromActivity.getFishingActivityLists().stream().findFirst();
            if (fishingActivitySummaryOptional.isPresent()) {
                FishingActivitySummary fishingActivitySummary = fishingActivitySummaryOptional.get();
                mapActivityFields(fishingActivity, activity, fishingActivitySummary);
                if (activity.isCorrection()) {
                    // update records with the same report id as not latest (this is the most recent correction)
                    activityRepository.updateOlderReportsAsNotLatest(activity.getFaReportId(), activity.getId());
                }
            }
        }
    }

    private void mapAsset(List<Asset> assets, Activity activity) {
        activity.setAsset(assets.stream().findFirst().orElse(null));
    }

    private void mapActivityFields(FishingActivity fishingActivity, Activity activity, FishingActivitySummary fishingActivitySummary) {
        activity.setPurposeCode(fishingActivitySummary.getPurposeCode());
        activity.setSource(fishingActivitySummary.getDataSource());
        activity.setCorrection(fishingActivitySummary.isIsCorrection());
        activity.setLatest(true);
        activity.setActivityId(String.valueOf(fishingActivitySummary.getActivityId()));
        Optional.ofNullable(fishingActivitySummary.getVesselContactParty()).ifPresent(vcp -> activity.setMaster(vcp.getGivenName()));
        activity.setAcceptedDate(Date.from(fishingActivitySummary.getAcceptedDateTime().toGregorianCalendar().toInstant()));
        activity.setCalculatedDate(calculateActivityDate(fishingActivitySummary, fishingActivity));
        activity.setOccurrenceDate(fishingActivitySummary.getOccurence() != null ? Date.from(fishingActivitySummary.getOccurence().toGregorianCalendar().toInstant()) : null);
        activity.setStartDate(getDate(fishingActivity, false));
        activity.setEndDate(getDate(fishingActivity, true));
    }

    private void mapBasicActivityFields(FishingActivity fishingActivity, String reportId, String wkt, String reportType, Activity activity) {
        activity.setActivityType(fishingActivity.getTypeCode().getValue());
        activity.setFaReportId(reportId);
        activity.setReportType(reportType);
        mapActivityCoordinates(fishingActivity, wkt, activity);

        Optional.ofNullable(fishingActivity.getReasonCode()).ifPresent(rc -> activity.setReasonCode(rc.getValue()));
    }

    private void mapActivityCoordinates(FishingActivity fishingActivity, String wkt, Activity activity) {
        activity.setActivityCoordinates(getMultipointGeometryFromWktString(wkt));
        if (activity.getActivityCoordinates() == null) {
            List<FishingActivity> relatedActivities = getGearRelatedActivitiesSorted(fishingActivity);
            Optional.ofNullable(relatedActivities).ifPresent(ra -> {
                ra.stream().filter(e -> e.getRelatedFLUXLocations() != null && e.getRelatedFLUXLocations().size() > 0)
                        .findFirst()
                        .ifPresent(f -> f.getRelatedFLUXLocations().stream().filter(rll -> rll.getTypeCode().getValue().equals("POSITION")).findFirst()
                                .ifPresent(loc -> {
                                    activity.setLongitude(loc.getSpecifiedPhysicalFLUXGeographicalCoordinate().getLongitudeMeasure().getValue().doubleValue());
                                    activity.setLatitude(loc.getSpecifiedPhysicalFLUXGeographicalCoordinate().getLatitudeMeasure().getValue().doubleValue());
                                }));
            });
        }
    }

    private void mapActivityLocations(FishingActivity fishingActivity, Activity activity) {
        Set<Location> locations = new HashSet<>();
        for (FLUXLocation relatedFLUXLocation : fishingActivity.getRelatedFLUXLocations()) {
            if (relatedFLUXLocation.getTypeCode().getValue().equals("POSITION")) {
                Location l = new Location();
                l.setLocationType(relatedFLUXLocation.getTypeCode().getValue());
                l.setLatitude(relatedFLUXLocation.getSpecifiedPhysicalFLUXGeographicalCoordinate().getLatitudeMeasure().getValue().doubleValue());
                l.setLongitude(relatedFLUXLocation.getSpecifiedPhysicalFLUXGeographicalCoordinate().getLongitudeMeasure().getValue().doubleValue());
                activityRepository.createLocation(l);
                locations.add(l);
            } else {
                Location l = activityRepository.findLocationByTypeCodeAndCode(mapLocationTypeCode(relatedFLUXLocation.getID().getSchemeID()), relatedFLUXLocation.getID().getValue());
                l = createLocationIfNotExists(relatedFLUXLocation, l);
                locations.add(l);
            }
        }
        activity.setLocations(locations);
    }

    private Location createLocationIfNotExists(FLUXLocation relatedFLUXLocation, Location l) {
        if (l == null) {
            l = new Location();
            l.setLocationType(relatedFLUXLocation.getTypeCode().getValue());
            l.setLocationTypeCode(mapLocationTypeCode(relatedFLUXLocation.getID().getSchemeID()));
            l.setLocationCode(relatedFLUXLocation.getID().getValue());

            Optional<String> locationCoordinatesFromSpatial = Optional.empty();
            if (l.getLocationType().equals("AREA")) {
                // locationCoordinatesFromSpatial = getLocationCoordinatesFromSpatial(relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode().getListID(), relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode().getValue());
            } else {
                locationCoordinatesFromSpatial = getLocationCoordinatesFromSpatial(l.getLocationTypeCode(), l.getLocationCode());
            }
            if (locationCoordinatesFromSpatial.isPresent()) {
                l.setLocationCoordinates(getMultipolygonGeometryFromWktString(locationCoordinatesFromSpatial.get()));
            }
            activityRepository.createLocation(l);
        }
        return l;
    }

    private Optional<String> getLocationCoordinatesFromSpatial(String areaTypeCode, String areaCode) {
        try {
            List<AreaSimpleType> areas = new ArrayList<>();
            AreaSimpleType areaSimpleType = new AreaSimpleType();
            areaSimpleType.setAreaType(areaTypeCode);
            areaSimpleType.setAreaCode(areaCode);
            areas.add(areaSimpleType);
            String areaByCodeRequest = SpatialModuleRequestMapper.mapToCreateGetAreaByCodeRequest(areas);
            String correlationId = spatialProducerBean.sendModuleMessage(areaByCodeRequest, reportingModuleReceiverBean.getDestination());
            TextMessage textMessage = reportingModuleReceiverBean.getMessage(correlationId, TextMessage.class, 60000L);
            AreaByCodeResponse response = JAXBMarshaller.unmarshall(textMessage, AreaByCodeResponse.class);
            return response.getAreaSimples().stream().findFirst().map(AreaSimpleType::getWkt);
        } catch (SpatialModelMarshallException | MessageException | ReportingModelException e) {
            log.error("Could not get port information for {} - {} from spatial", areaTypeCode, areaCode, e);
            return Optional.empty();
        }
    }

    private String getCfrFromVesselTransportMeans(VesselTransportMeans vtm) {
        Optional<IDType> cfr = vtm.getIDS().stream().filter(id -> id.getSchemeID().equals("CFR")).findAny();
        return cfr.map(IDType::getValue).orElse(null);
    }

    private void mapSpecies(FishingActivity fishingActivity, Activity activity, List<Asset> assets) {
        List<Catch> catches = new ArrayList<>();
        List<FACatch> catchesToProcess = fishingActivity.getSpecifiedFACatches();

        if (fishingActivity.getTypeCode().getValue().equals("JOINT_FISHING_OPERATION")) {
            Optional<Asset> asset = assets.stream().findFirst();
            final String cfrToMatch = asset.map(Asset::getCfr).orElse("");
            List<FishingActivity> relatedFishingActivitiesForCfr = findRelatedFishingActivitiesForJFOwithCFR(fishingActivity, cfrToMatch);

            Optional<FishingActivity> first = relatedFishingActivitiesForCfr.stream().findFirst();
            if (first.isPresent()) {
                catchesToProcess = first.get().getSpecifiedFACatches();
            }
        }

        for (FACatch faCatch : catchesToProcess) {
            Catch speciesCatch = new Catch();
            setBaseCatchInfo(faCatch, speciesCatch);
            // get this from root specified catch
            fishingActivity.getSpecifiedFACatches().stream().findFirst().ifPresent(rsc -> {
                setProcessingCatchInfo(speciesCatch, rsc);
            });
            speciesCatch.setActivity(activity);
            activityRepository.createCatchEntity(speciesCatch);

            catches.add(speciesCatch);
        }
        activity.setSpeciesCatch(catches);
    }

    private List<FishingActivity> findRelatedFishingActivitiesForJFOwithCFR(FishingActivity fishingActivity, String cfrToMatch) {
        return fishingActivity.getRelatedFishingActivities().stream().filter(rfa -> {
            boolean matches = false;
            for (int i = 0; i < rfa.getRelatedVesselTransportMeans().size(); i++) {
                if (getCfrFromVesselTransportMeans(rfa.getRelatedVesselTransportMeans().get(i)).equals(cfrToMatch)) {
                    matches = true;
                    break;
                }
            }
            return matches;
        }).collect(Collectors.toList());
    }

    private void setProcessingCatchInfo(Catch speciesCatch, FACatch rsc) {
        Optional.ofNullable(rsc.getSpecifiedSizeDistribution()).ifPresent(sd -> Optional.ofNullable(sd.getClassCodes()).ifPresent(cd -> cd.stream().findAny().ifPresent(c -> speciesCatch.setSizeClass(c.getValue()))));
        Optional.ofNullable(rsc.getSpecifiedSizeDistribution()).ifPresent(sd -> Optional.ofNullable(sd.getCategoryCode()).ifPresent(cat -> speciesCatch.setSizeCategory(cat.getValue())));
        rsc.getAppliedAAPProcesses().stream().findFirst().ifPresent(ap -> {
            ap.getTypeCodes().stream().filter(tc -> tc.getListID().equals("FISH_PRESERVATION")).findAny().ifPresent(pr -> speciesCatch.setPreservation(pr.getValue()));
            ap.getTypeCodes().stream().filter(tc -> tc.getListID().equals("FISH_PRESENTATION")).findAny().ifPresent(pr -> speciesCatch.setPresentation(pr.getValue()));
            speciesCatch.setCf(ap.getConversionFactorNumeric().getValue().doubleValue());

            ap.getResultAAPProducts().stream().findFirst().ifPresent(p -> {
                speciesCatch.setProductWeightMeasureUnitCode(p.getWeightMeasure().getUnitCode());
                speciesCatch.setProductWeightMeasure(p.getWeightMeasure().getValue().doubleValue());
                speciesCatch.setProductQuantity(p.getPackagingUnitQuantity().getValue().doubleValue());
            });
        });
    }

    private void setBaseCatchInfo(FACatch faCatch, Catch speciesCatch) {
        speciesCatch.setSpeciesCode(faCatch.getSpeciesCode().getValue());
        speciesCatch.setCatchType(faCatch.getTypeCode().getValue());
        speciesCatch.setWeightMeasureUnitCode(faCatch.getWeightMeasure().getUnitCode());
        speciesCatch.setWeightMeasure(faCatch.getWeightMeasure().getValue().doubleValue());
        Optional.ofNullable(faCatch.getUnitQuantity()).ifPresent(q -> speciesCatch.setQuantity(q.getValue().doubleValue()));
        Set<CatchLocation> locations = new HashSet<>();
        faCatch.getSpecifiedFLUXLocations().stream().forEach(l -> {
            CatchLocation loc = new CatchLocation();
            loc.setLocationTypeCode(l.getTypeCode().getValue());
            Optional.ofNullable(l.getID()).ifPresent(locId -> {
                loc.setLocationType(locId.getSchemeID());
                loc.setLocationCode(locId.getValue());
            });
            Optional.ofNullable(l.getSpecifiedPhysicalFLUXGeographicalCoordinate()).ifPresent(pl -> {
                loc.setLongitude(pl.getLongitudeMeasure().getValue().doubleValue());
                loc.setLongitude(pl.getLatitudeMeasure().getValue().doubleValue());
            });

            activityRepository.createActivityCatchLocation(loc);
            locations.add(loc);
        });
        speciesCatch.setLocations(locations);
    }

    private void mapFishingGear(FishingActivity fishingActivity, Activity activity) {
        Set<String> gearTypes = new HashSet<>();
        for (FishingGear g : fishingActivity.getSpecifiedFishingGears()) {
            gearTypes.add(g.getTypeCode().getValue());
        }

        if (gearTypes.size() == 0) { // get gear type from related fishing activities
            List<FishingActivity> relatedActivities = getGearRelatedActivitiesSorted(fishingActivity);
            Optional.ofNullable(getValidGearTypes(relatedActivities)).ifPresent(gearTypes::addAll);
        }
        activity.setGears(gearTypes);
    }

    private List<String> getValidGearTypes(List<FishingActivity> relatedActivities) {
        List<String> gearTypes = null;
        if (relatedActivities.size() > 0) {
            for (int i = 0; i < relatedActivities.size(); i++) {
                List<String> types = relatedActivities.get(i).getSpecifiedFishingGears().stream()
                        .filter(fg -> fg.getTypeCode() != null)
                        .map(fg -> fg.getTypeCode().getValue())
                        .collect(Collectors.toList());
                if (types != null && types.size() > 0) {
                    gearTypes = types;
                    break;
                }
            }
        }
        return gearTypes;
    }

    private List<FishingActivity> getGearRelatedActivitiesSorted(FishingActivity fishingActivity) {
        return fishingActivity.getRelatedFishingActivities().stream()
                .filter(fa -> Arrays.asList("GEAR_SHOT", "GEAR_RETRIEVAL").contains(fa.getTypeCode().getValue()))
                .sorted(Comparator.comparing(fa -> fa.getTypeCode().getValue()))
                .collect(Collectors.toList());
    }

    private Date calculateActivityDate(FishingActivitySummary fishingActivitySummary, FishingActivity fishingActivity) {
        if (REPORT_TYPE_DECLARATION.equals(fishingActivitySummary.getReportType())) {
            if (fishingActivitySummary.getOccurence() != null) {
                return Date.from(fishingActivitySummary.getOccurence().toGregorianCalendar().toInstant());
            }
            Optional<DelimitedPeriod> period = fishingActivity.getSpecifiedDelimitedPeriods().stream().findFirst();
            if (period.isPresent()) {
                if (period.get().getEndDateTime() != null) {
                    return Date.from(period.get().getEndDateTime().getDateTime().toGregorianCalendar().toInstant());
                } else if (period.get().getStartDateTime() != null) {
                    return Date.from(period.get().getStartDateTime().getDateTime().toGregorianCalendar().toInstant());
                }
            }
            if (fishingActivity.getRelatedFishingActivities() != null) {
                Optional<FishingActivity> gearRetrieval = fishingActivity.getRelatedFishingActivities().stream().filter(rfa -> rfa.getTypeCode().getValue().equals("GEAR_RETRIEVAL")).findFirst();
                Optional<FishingActivity> gearShot = fishingActivity.getRelatedFishingActivities().stream().filter(rfa -> rfa.getTypeCode().getValue().equals("GEAR_SHOT")).findFirst();
                if (gearRetrieval.isPresent()) {
                    return Date.from(gearRetrieval.get().getOccurrenceDateTime().getDateTime().toGregorianCalendar().toInstant());
                }
                if (gearShot.isPresent()) {
                    return Date.from(gearShot.get().getOccurrenceDateTime().getDateTime().toGregorianCalendar().toInstant());
                }
            }

        }
        // otherwise return accepted date/time as in NOTIFICATION report type
        return Date.from(fishingActivitySummary.getAcceptedDateTime().toGregorianCalendar().toInstant());
    }

    private Date getDate(FishingActivity fishingActivity, boolean getEndDate) {
        Optional<DelimitedPeriod> period = fishingActivity.getSpecifiedDelimitedPeriods().stream().findFirst();
        if (period.isPresent()) {
            if (getEndDate && period.get().getEndDateTime() != null) {
                return Date.from(period.get().getEndDateTime().getDateTime().toGregorianCalendar().toInstant());
            } else if (period.get().getStartDateTime() != null) {
                return Date.from(period.get().getStartDateTime().getDateTime().toGregorianCalendar().toInstant());
            }
        }
        if (fishingActivity.getRelatedFishingActivities() != null) {
            if (getEndDate) {
                Optional<FishingActivity> gearRetrieval = fishingActivity.getRelatedFishingActivities().stream().filter(rfa -> rfa.getTypeCode().getValue().equals("GEAR_RETRIEVAL")).findFirst();
                if (gearRetrieval.isPresent()) {
                    return Date.from(gearRetrieval.get().getOccurrenceDateTime().getDateTime().toGregorianCalendar().toInstant());
                }
            } else {
                Optional<FishingActivity> gearShot = fishingActivity.getRelatedFishingActivities().stream().filter(rfa -> rfa.getTypeCode().getValue().equals("GEAR_SHOT")).findFirst();
                if (gearShot.isPresent()) {
                    return Date.from(gearShot.get().getOccurrenceDateTime().getDateTime().toGregorianCalendar().toInstant());
                }
            }
        }
        return null;
    }

    private void mapTripId(FishingTrip fishingTrip, Trip trip) {
        for (IDType id : fishingTrip.getIDS()) {
            trip.setTripIdScheme(id.getSchemeID());
            trip.setTripId(id.getValue());
        }
    }

    private Trip createFishingTrip(FishingTrip fishingTrip, List<Asset> assets, FishingTripResponse fishingTripData) {
        Trip trip = new Trip();
        mapTripId(fishingTrip, trip);
        trip.setAsset(assets.stream().findFirst().orElse(null));

        fishingTripData.getFishingTripIdLists().stream().filter(t -> t.getTripId().equalsIgnoreCase(trip.getTripId())).findFirst().ifPresent(t -> {
            trip.setFirstFishingActivityDate(Date.from(t.getFirstFishingActivityDateTime().toGregorianCalendar().toInstant()));
            trip.setLastFishingActivityDate(Date.from(t.getLastFishingActivityDateTime().toGregorianCalendar().toInstant()));
            trip.setFirstFishingActivity(t.getFirstFishingActivity());
            trip.setLastFishingActivity(t.getLastFishingActivity());
            trip.setNumberOfCorrections(t.getNoOfCorrections());
            trip.setTripDuration(t.getTripDuration());
            trip.setMultipointWkt(getMultipointGeometryFromWktString(t.getGeometry()));
        });
        activityRepository.createTripEntity(trip);
        return trip;
    }

    private MultiPoint getMultipointGeometryFromWktString(String wkt) {
        try {
            MultiPoint geometry = (MultiPoint) wktReader.read(wkt);
            geometry.setSRID(4326);
            return geometry;
        } catch (ParseException e) {
            log.error("unable to set geometry from wkt string: {}", wkt);
        } catch (NullPointerException e) {
            log.error("Error occurred with wkt string: {}, possibly no coordinates available", wkt, e);
        }
        return null;
    }

    private MultiPolygon getMultipolygonGeometryFromWktString(String wkt) {
        try {
            MultiPolygon geometry = (MultiPolygon) wktReader.read(wkt);
            geometry.setSRID(4326);
            return geometry;
        } catch (ParseException e) {
            log.error("unable to set geometry from wkt string: {}", wkt);
            return null;
        }
    }

    private FishingTripResponse getTripDetailsFromActivity(String tripId) {
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(ISODateTimeFormat.dateTimeNoMillis()).toFormatter().withOffsetParsed();
            List<SingleValueTypeFilter> singleFilters = Arrays.asList(
                    new SingleValueTypeFilter(SearchFilter.TRIP_ID, tripId),
                    new SingleValueTypeFilter(SearchFilter.PERIOD_START, formatter.print(DateTime.now().minusYears(3).toLocalDateTime())),
                    new SingleValueTypeFilter(SearchFilter.PERIOD_END, formatter.print(DateTime.now().toLocalDateTime())));
            String request = ActivityModuleRequestMapper.mapToActivityGetFishingTripRequest(Collections.EMPTY_LIST, singleFilters);
            String correlationId = activityModuleSenderBean.sendModuleMessageNonPersistent(request, reportingModuleReceiverBean.getDestination(), 60000L);
            TextMessage receivedMessageAsTextMessage = reportingModuleReceiverBean.getMessage(correlationId, TextMessage.class, 60000L);
            return JAXBMarshaller.unmarshall(receivedMessageAsTextMessage, FishingTripResponse.class);
        } catch (ActivityModelMarshallException | MessageException | ReportingModelException e) {
            log.error("Error getting fishing trip information");
            return null;
        }
    }

    private String mapLocationTypeCode(String value) {
        return "LOCATION".equals(value) ? "PORT_AREA" : value;
    }
}
