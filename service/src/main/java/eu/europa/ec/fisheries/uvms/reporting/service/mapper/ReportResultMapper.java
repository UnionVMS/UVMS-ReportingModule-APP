package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierSchemeIdEnum;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;
import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ActivityDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FishingActivitySummaryDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TripDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ActivityReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.MovementReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportResult;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetHistoryId;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetId;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetIdType;
import org.geotools.feature.DefaultFeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ReportResultMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ReportResultMapper.class);
    private enum FIELD_TYPE {AREA, PORT, SPECIES, GEAR};

    private WKTWriter wktWriter;

    public ReportResultMapper() {
        wktWriter = new WKTWriter();
    }

    public ExecutionResultDTO map(ReportResult reportResult, DisplayFormat displayFormat) {
        ExecutionResultDTO executionResultDTO = new ExecutionResultDTO();

        mapTracks(reportResult, displayFormat, executionResultDTO);
        mapSegments(reportResult, displayFormat, executionResultDTO);
        mapMovements(reportResult, displayFormat, executionResultDTO);
        mapActivities(reportResult, executionResultDTO);
        mapTrips(reportResult, executionResultDTO);
        return executionResultDTO;
    }

    private void mapMovements(ReportResult reportResult, DisplayFormat displayFormat, ExecutionResultDTO executionResultDTO) {
        DefaultFeatureCollection movements = new DefaultFeatureCollection(null, MovementDTO.SIMPLE_FEATURE_TYPE);
        executionResultDTO.setMovements(movements);
        reportResult.getMovementReportResult().forEach(m -> {
            try {
                MovementType movementType = new MovementType();
                movementType.setReportedCourse(m.getReportedCourse());
                movementType.setReportedSpeed(m.getReportedSpeed());
                movementType.setCalculatedSpeed(m.getCalculatedSpeed());
                movementType.setWkt(wktWriter.write(m.getPosition()));
                movementType.setConnectId(m.getConnectId());
                movementType.setSource(MovementSourceType.fromValue(m.getSource()));

                MovementActivityType movementActivityType = new MovementActivityType();
                movementActivityType.setMessageType(MovementActivityTypeType.fromValue(m.getMovementActivityType()));
                movementType.setActivity(movementActivityType);

                movementType.setMovementType(MovementTypeType.fromValue(m.getMovementType()));
                movementType.setGuid(m.getMovementGuid());
                MovementPoint mp = new MovementPoint();
                mp.setLongitude(m.getPosition().getX());
                mp.setLatitude(m.getPosition().getY());
                movementType.setPosition(mp);

                movementType.setPositionTime(m.getPositionTime());

                Asset asset = makeAssetFromReportData(m);
                MovementDTO movementDTO = new MovementDTO(movementType, asset, displayFormat);

                movements.add(movementDTO.toFeature());
            } catch (Exception e) {
                LOG.warn("Error creating movement from report data: " + m.toString(), e);
            }

        });
    }



    private void mapActivities(ReportResult reportResult, ExecutionResultDTO executionResultDTO) {

        if(reportResult.getActivityReportResult() == null || reportResult.getActivityReportResult().isEmpty()){
            return;
        }


        DefaultFeatureCollection activities = new DefaultFeatureCollection(null, ActivityDTO.ACTIVITY);
        executionResultDTO.setActivities(activities);
        List<FishingActivitySummaryDTO> activityList = new ArrayList<>();
        int arraySize = reportResult.getActivityReportResult().size();
        ActivityReportResult[] array = new ActivityReportResult[arraySize];
        reportResult.toString();
        ActivityReportResult[] activityReportResults = reportResult.getActivityReportResult().toArray(array);

        Map<String, Set<String>> areasMap = new HashMap<>();
        Map<String, Set<String>> portMap = new HashMap<>();
        Map<String, Set<String>> speciesMap = new HashMap<>();
        Map<String, Set<String>> gearsMap = new HashMap<>();

        for(int i = 0; i < arraySize; i++ ) {

            addElementToMap(activityReportResults[i],areasMap,FIELD_TYPE.AREA);
            addElementToMap(activityReportResults[i],portMap,FIELD_TYPE.PORT);
            addElementToMap(activityReportResults[i],speciesMap,FIELD_TYPE.SPECIES);
            addElementToMap(activityReportResults[i],gearsMap,FIELD_TYPE.GEAR);

            if((i + 1) == arraySize || !activityReportResults[i].getId().equals(activityReportResults[i + 1].getId())){
                FishingActivitySummaryDTO summary = makeActivityFromReportData(activityReportResults[i]);
                summary.setSpecies(new ArrayList<>(areasMap.get(activityReportResults[i].getActivityId())));
                summary.setPorts(new ArrayList<>(portMap.get(activityReportResults[i].getActivityId())));
                summary.setAreas(new ArrayList<>(speciesMap.get(activityReportResults[i].getActivityId())));
                summary.setGears(new ArrayList<>(gearsMap.get(activityReportResults[i].getActivityId())));
                ActivityDTO fishingActivityDTO = new ActivityDTO(summary);
                activityList.add(summary);
                try {
                    activities.add(fishingActivityDTO.toFeature());
                } catch (ParseException e) {
                    LOG.warn("Error creating activity from report data: " + e.toString(), e);
                }
            }
        }
        executionResultDTO.setActivityList(activityList);
    }

    private void addElementToMap(ActivityReportResult activityReportResult,Map<String, Set<String>> mapToProcess,FIELD_TYPE type){
        if(activityReportResult.getActivityId() == null){
            return;
        }

        if(mapToProcess.get(activityReportResult.getActivityId()) == null){
            Set<String> values = addElementToSet(activityReportResult, new HashSet<>(), type);
            mapToProcess.put(activityReportResult.getActivityId(),values);
        } else {
            Set<String> values = mapToProcess.get(activityReportResult.getActivityId());
            Set<String> updatedValues = addElementToSet(activityReportResult, values, type);
            mapToProcess.put(activityReportResult.getActivityId(),updatedValues);
        }
    }

    private Set<String> addElementToSet(ActivityReportResult activityReportResult,Set<String> set, FIELD_TYPE type){

        switch(type) {
            case AREA:
                if(!"LOCATION".equals(activityReportResult.getCatchLocationType())){
                    set.add(activityReportResult.getLocationCode());}
                break;
            case PORT:
                if("LOCATION".equals((activityReportResult.getLocationType()))){
                    set.add(activityReportResult.getLocationTypeCode());}
                break;
            case SPECIES:
                set.add(activityReportResult.getSpeciesCode());
                break;
            case GEAR:
                set.add(activityReportResult.getGear());
                break;
            default:
                return  set;
        }

        return set;
    }

    private FishingActivitySummaryDTO makeActivityFromReportData(ActivityReportResult r) {
        FishingActivitySummaryDTO fishingActivity = new FishingActivitySummaryDTO();
        fishingActivity.setAcceptedDateTime(r.getAcceptedDate() == null ? null : XMLDateUtils.dateToXmlGregorian(r.getAcceptedDate()) );
        fishingActivity.setActivityId(r.getActivityId() == null ? null : Integer.valueOf(r.getActivityId()));
        fishingActivity.setActivityType(r.getActivityType());
        fishingActivity.setDataSource(r.getSource());
        fishingActivity.setFaReportID(r.getFaReportId() == null ? null : Integer.valueOf(r.getFaReportId()));
        fishingActivity.setFlagState(r.getCountryCode());
        fishingActivity.setGeometry(r.getActivityCoordinates() == null? null : r.getActivityCoordinates().toString());
        fishingActivity.setCorrection(r.getPurposeCode() == null ? false : Integer.valueOf(r.getPurposeCode()) == 9);
//        fishingActivity.setLandingReferencedID();
//        fishingActivity.setLandingState();
        fishingActivity.setAcceptedDateTime(r.getOccurrenceDate() == null ? null : XMLDateUtils.dateToXmlGregorian(r.getOccurrenceDate()));
        fishingActivity.setPurposeCode(r.getPurposeCode());
        fishingActivity.setReportType(r.getReportType());
        fishingActivity.setTripId(r.getTripId());
//        fishingActivity.setVesselContactParty();
        fishingActivity.setVesselGuid(r.getAssetGuid());
        fishingActivity.setVesselIdentifiers(vesselIdentifierTypes(r));
        fishingActivity.setVesselName(r.getName());
        return fishingActivity;
    }

    private void mapTrips(ReportResult reportResult, ExecutionResultDTO executionResultDTO) {

        if(reportResult.getActivityReportResult() == null || reportResult.getActivityReportResult().isEmpty()){
            return;
        }

        List<TripDTO> trips = new ArrayList<>();
        reportResult.getActivityReportResult().forEach(m -> {
            TripDTO tripDTO = new TripDTO();
            tripDTO.setFirstFishingActivity(m.getFirstFishingActivity());
            tripDTO.setFirstFishingActivityDateTime(m.getFirstFishingActivityDate());
//            tripDTO.setFlagState();
            tripDTO.setGeometry(m.getCoordinates() == null ? null : m.getCoordinates().toString());
            tripDTO.setLastFishingActivity(m.getLastFishingActivity());
            tripDTO.setLastFishingActivityDateTime(m.getLastFishingActivityDate());
            tripDTO.setNoOfCorrections(m.getNumberOfCorrections());
//            tripDTO.setRelativeFirstFaDateTime();
//            tripDTO.setRelativeLastFaDateTime();
            tripDTO.setSchemeId(m.getTripIdScheme());
            tripDTO.setTripDuration(m.getTripDuration());
            tripDTO.setTripId(m.getTripId());
            tripDTO.setVesselIdLists(vesselIdentifierTypes(m));
            tripDTO.setVmsPositionCount(m.getPositionCount());
            trips.add(tripDTO);
        });

        executionResultDTO.setTrips(trips);
    }

    private List<VesselIdentifierType> vesselIdentifierTypes(ActivityReportResult activityReportResult){
        List<VesselIdentifierType> listToReturn = new ArrayList<>();

        VesselIdentifierType vesselIdentifierCFR = new VesselIdentifierType();
        vesselIdentifierCFR.setKey(VesselIdentifierSchemeIdEnum.CFR);
        vesselIdentifierCFR.setValue(activityReportResult.getCfr());
        listToReturn.add(vesselIdentifierCFR);

        VesselIdentifierType vesselIdentifierGFCM = new VesselIdentifierType();
        vesselIdentifierGFCM.setKey(VesselIdentifierSchemeIdEnum.GFCM);
        vesselIdentifierGFCM.setValue(activityReportResult.getGfcm());
        listToReturn.add(vesselIdentifierGFCM);

        VesselIdentifierType vesselIdentifierIccat = new VesselIdentifierType();
        vesselIdentifierIccat.setKey(VesselIdentifierSchemeIdEnum.ICCAT);
        vesselIdentifierIccat.setValue(activityReportResult.getIccat());
        listToReturn.add(vesselIdentifierIccat);

        VesselIdentifierType vesselIdentifierIrcs = new VesselIdentifierType();
        vesselIdentifierIrcs.setKey(VesselIdentifierSchemeIdEnum.IRCS);
        vesselIdentifierIrcs.setValue(activityReportResult.getIrcs());
        listToReturn.add(vesselIdentifierIrcs);

        VesselIdentifierType vesselIdentifierUvi = new VesselIdentifierType();
        vesselIdentifierUvi.setKey(VesselIdentifierSchemeIdEnum.UVI);
        vesselIdentifierUvi.setValue(activityReportResult.getUvi());
        listToReturn.add(vesselIdentifierUvi);
        return listToReturn;
    }

    private void mapSegments(ReportResult reportResult, DisplayFormat displayFormat, ExecutionResultDTO executionResultDTO) {
        DefaultFeatureCollection segments = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);
        executionResultDTO.setSegments(segments);
        reportResult.getMovementReportResult().forEach(s -> {
            try {
                MovementSegment segment = new MovementSegment();
                segment.setTrackId(s.getTrackId().toString());
                segment.setSpeedOverGround(s.getSegmentSpeedOverGround());
                segment.setCourseOverGround(s.getSegmentCourseOverGround());
                segment.setDuration(s.getSegmentDuration());
                segment.setDistance(s.getSegmentDistance());
                segment.setCategory(SegmentCategoryType.fromValue(s.getSegmentCategory()));
                segment.setId(s.getSegmentId().toString());
                segment.setWkt(wktWriter.write(s.getSegmentCoordinates()));
                Asset asset = makeAssetFromReportData(s);
                SegmentDTO segmentDTO = new SegmentDTO(segment, asset, displayFormat);

                segments.add(segmentDTO.toFeature());
            } catch (Exception e) {
                LOG.warn("Error creating segment from report data: " + s.toString(), e);
            }

        });
    }

    private void mapTracks(ReportResult reportResult, DisplayFormat displayFormat, ExecutionResultDTO executionResultDTO) {
        List<TrackDTO> trackDTOList = new ArrayList<>();
        executionResultDTO.setTracks(trackDTOList);
        reportResult.getMovementReportResult().forEach(r -> {
            try {
                MovementTrack movementTrack = makeMovementTrackFromReportResult(r);
                Asset asset = makeAssetFromReportData(r);
                TrackDTO trackDTO = new TrackDTO(movementTrack, asset, r.getTrackExtent(), r.getTrackNearestPoint(), displayFormat);
                trackDTOList.add(trackDTO);
            } catch (Exception e) {
                LOG.warn("Error creating track from report data: " + r.toString(), e);
            }
        });
    }

    private MovementTrack makeMovementTrackFromReportResult(MovementReportResult r) {
        MovementTrack movementTrack = new MovementTrack();
        movementTrack.setTotalTimeAtSea(r.getTrackTotalTimeAtSea());
        movementTrack.setDuration(r.getTrackDuration());
        movementTrack.setDistance(r.getTrackDistance());
        movementTrack.setId(r.getTrackId().toString());
        movementTrack.setWkt(null);
        return movementTrack;
    }

    private Asset makeAssetFromReportData(MovementReportResult r) {
        Asset asset = new Asset();
        asset.setCfr(r.getCfr());
        asset.setIrcs(r.getIrcs());
        asset.setIccat(r.getIccat());
        asset.setGfcm(r.getGfcm());
        asset.setUvi(r.getUvi());
        asset.setExternalMarking(r.getExternalMarking());
        asset.setName(r.getName());
        asset.setCountryCode(r.getCountryCode());

        asset.setLengthOverAll(BigDecimal.valueOf(r.getLengthOverall()));
        asset.setGearType(r.getMainGearType());

        AssetHistoryId assetHistoryId = new AssetHistoryId();
        assetHistoryId.setEventId(r.getAssetHistGuid());
        asset.setEventHistory(assetHistoryId);

        AssetId assetId = new AssetId();
        assetId.setType(AssetIdType.GUID);
        assetId.setGuid(r.getAssetGuid());
        assetId.setValue(r.getAssetGuid());
        asset.setAssetId(assetId);
        return asset;
    }
}
