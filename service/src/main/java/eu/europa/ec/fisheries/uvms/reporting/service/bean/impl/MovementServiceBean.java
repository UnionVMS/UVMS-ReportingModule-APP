/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentAndTrackList;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentIds;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.movement.MovementClient;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.util.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.AssetRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Movement;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Segment;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Track;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.MovementMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetHistoryId;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetId;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetIdType;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteria;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListPagination;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import eu.europa.ec.fisheries.wsdl.asset.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.user.types.UserFault;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Slf4j
public class MovementServiceBean {

    @EJB
    private AssetServiceBean assetServiceBean;

    @EJB
    private MovementModuleSenderBean movementSender;

    @EJB
    private ReportingModuleReceiverBean receiver;

    @Inject
    private MovementRepositoryBean movementRepositoryBean;

    @Inject
    private MovementMapper movementMapper;

    @Inject
    private MovementClient movementClient;

    @Inject
    private AssetRepository assetRepository;

    private final WKTReader wktReader = new WKTReader();


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Interceptors(SimpleTracingInterceptor.class)
    public Map<String, MovementMapResponseType> getMovementMap(FilterProcessor processor) throws ReportingServiceException {
        return ExtMovementMessageMapper.getMovementMap(getMovementMapResponseTypes(processor));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<MovementMapResponseType> getMovement(FilterProcessor processor) throws ReportingServiceException {
        log.trace("getMovement({})", processor.toString());
        return getMovementMapResponseTypes(processor);
    }

    /**
     * Creates Movement,Segment and Track entities enriched with Asset,Movement module data
     *
     * @param movementTypes List of MovementType to be mapped to Movements
     * @throws ReportingServiceException Thrown if error occurs
     */
    public void createMovementsSegmentsAndTracks(List<MovementType> movementTypes) throws ReportingServiceException {
        Map<String, Asset> assetMap = getFromAssetIfNotAvailableLocally(movementTypes);
        Map<String, MovementTypeData> movementTypeDataMap = movementTypes.stream()
                .map(movementType -> new MovementTypeData(movementType, assetMap.get(movementType.getConnectId())))
                .collect(Collectors.toMap(toMovementGuid(), Function.identity()));
        movementTypeDataMap.values().forEach(this::createMovement);
        createSegmentsAndTracks(movementTypes, movementTypeDataMap);
    }

    /**
     * Generates Segment and Tracks based on segment id list
     *
     * @param movementTypes The MovementType list
     */
    public void createSegmentsAndTracks(List<MovementType> movementTypes, Map<String, MovementTypeData> movementTypeDataMap) {
        List<SegmentIds> segmentIds = movementTypes.stream()
                .map(toSegmentId())
                .collect(Collectors.toList());
        segmentIds = segmentIds.stream().filter(s -> s.getSegmentIds().size() > 0).collect(Collectors.toList());
        if (segmentIds.size() > 0) {
            List<SegmentAndTrackList> segmentAndTrackList = movementClient.getSegmentsAndTrackBySegmentIds(segmentIds);
            if (segmentAndTrackList != null) {
                segmentAndTrackList.forEach(segmentAndTrackListItem -> {
                    MovementTypeData movementTypeData = movementTypeDataMap.get(segmentAndTrackListItem.getMoveGuid());
                    // normally this asset can be retrieved once before the loop or passed on through the caller of this function createSegmentsAndTracks
                    eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset asset = assetRepository.findAssetByAssetHistoryGuid(movementTypeData.movementType.getConnectId());
                    segmentAndTrackListItem.getSegmentAndTrackList().forEach(segmentAndTrack -> {
                        createSegment(segmentAndTrack.getSegment(), movementTypeData, asset);
                        createTrack(segmentAndTrack.getTrack(), asset);
                    });
                });
            }
        }
    }

    private static Function<MovementType, SegmentIds> toSegmentId() {
        return (mt) -> {
            SegmentIds segmentId = new SegmentIds();
            segmentId.setMoveGuid(mt.getGuid());
            segmentId.getSegmentIds().addAll(mt.getSegmentIds().stream().map(Long::parseLong).collect(Collectors.toList()));
            return segmentId;
        };
    }

    private Movement createMovement(MovementTypeData movementTypeData) {
        Movement movement = movementMapper.toMovement(movementTypeData.movementType);
        movement.setMovementGuid(movementTypeData.movementType.getGuid());
        movement.setAsset(assetRepository.findAssetByAssetHistoryGuid(movementTypeData.movementType.getConnectId()));
        enrichMovementWithAreas(movementTypeData, movement);
        return movementRepositoryBean.createMovementEntity(movement);
    }

    private void enrichMovementWithAreas(MovementTypeData movementTypeData, Movement movement) {
        movement.setClosestCountry(movementTypeData.movementType.getMetaData().getClosestCountry().getCode());
        movement.setClosestCountryDistance(movementTypeData.movementType.getMetaData().getClosestCountry().getDistance());
        movement.setClosestPort(movementTypeData.movementType.getMetaData().getClosestPort().getCode());
        movement.setClosestPortDistance(movementTypeData.movementType.getMetaData().getClosestPort().getDistance());
        Optional.ofNullable(movementTypeData.movementType.getActivity())
                .ifPresent(activityType -> Optional.ofNullable(activityType.getMessageType())
                        .ifPresent(mt -> movement.setMovementActivityType(activityType.getMessageType().value())));
        Set<Area> areas = new HashSet<>();

        movementTypeData.movementType.getMetaData().getAreas().forEach(area -> {
            Area a = movementRepositoryBean.findAreaByTypeAndAreaCode(area.getAreaType(), area.getCode());
            if (a == null) {
                a = new Area();
                a.setAreaType(area.getAreaType());
                a.setAreaCode(area.getCode());
                a.setAreaName(area.getName());
                a.setRemoteId(area.getRemoteId());
                movementRepositoryBean.createArea(a);
                areas.add(a);
            } else {
                areas.add(a);
            }
        });
        movement.setAreas(areas);
    }

    private Segment createSegment(MovementSegment movementSegment, MovementTypeData movementTypeData, eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset asset) {
        Segment segment = movementMapper.toSegment(movementSegment);
        segment.setMovementGuid(movementTypeData.movementType.getGuid());
        segment.setAsset(asset);
        return movementRepositoryBean.createSegmentEntity(segment);
    }

    private Track createTrack(MovementTrack movementTrack, eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset asset) {
        Track track = movementMapper.toTrack(movementTrack);
        track.setAsset(asset);
        LineString geometry = getLineStringGeometryFromWktString(movementTrack.getWkt());
        track.setNearestPoint(calculateNearestPoint(geometry));
        track.setExtent(calculateExtent(geometry));
        return movementRepositoryBean.createTrackEntity(track);
    }

    private Map<String, Asset> getFromAssetIfNotAvailableLocally(List<MovementType> movementTypes) throws ReportingServiceException {
        boolean found;
        List<eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset> assetsFromDb = new ArrayList<>();
        found = getAssetsFromDb(movementTypes, assetsFromDb);

        List<Asset> assets;
        if (found) {
            assets = assetsFromDb.stream().map(this::mapFromAssetEntity).collect(Collectors.toList());
        } else {
            AssetListQuery query = new AssetListQuery();
            AssetListCriteria value = new AssetListCriteria();
            List<AssetListCriteriaPair> criteriaPairs = value.getCriterias();
            for (MovementType movementType : movementTypes) {
                AssetListCriteriaPair criteriaPair = new AssetListCriteriaPair();
                criteriaPair.setKey(ConfigSearchField.HIST_GUID);
                criteriaPair.setValue(movementType.getConnectId());
                criteriaPairs.add(criteriaPair);
            }
            value.setIsDynamic(false);
            query.setPagination(new AssetListPagination());
            query.getPagination().setPage(1);
            query.getPagination().setListSize(100);
            query.setAssetSearchCriteria(value);
            assets = assetServiceBean.getAssets(query);

            assets.forEach(a -> {
                eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset asset = mapFromAsset(a);
                assetRepository.createAssetEntity(asset);
            });
        }
        return assets.stream().collect(Collectors.toMap(toAssetGuid(), Function.identity()));
    }

    private boolean getAssetsFromDb(List<MovementType> movementTypes, List<eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset> assetsFromDb) {
        boolean found = false;
        for (int i = 0; i < movementTypes.size(); i++) {
            Optional<eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset> assetByGuid = Optional.ofNullable(assetRepository.findAssetByAssetHistoryGuid(movementTypes.get(i).getConnectId()));
            if (assetByGuid.isPresent()) {
                assetsFromDb.add(assetByGuid.get());
                found = true;
            } else {
                found = false;
                break;
            }
        }
        return found;
    }

    private Asset mapFromAssetEntity(eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset a) {
        Asset asset = new Asset();
        asset.setCfr(a.getCfr());
        asset.setIrcs(a.getIrcs());
        asset.setIccat(a.getIccat());
        asset.setUvi(a.getUvi());
        asset.setGfcm(a.getGfcm());
        asset.setExternalMarking(a.getExternalMarking());
        asset.setName(a.getName());
        asset.setCountryCode(a.getCountryCode());
        asset.setGearType(a.getMainGearType());
        Optional.ofNullable(a.getLengthOverall()).ifPresent(l -> {
            asset.setLengthOverAll(BigDecimal.valueOf(a.getLengthOverall()));
        });

        AssetId assetId = new AssetId();
        assetId.setGuid(a.getAssetGuid());
        assetId.setType(AssetIdType.GUID);
        asset.setAssetId(assetId);
        AssetHistoryId assetHistoryId = new AssetHistoryId();
        assetHistoryId.setEventId(a.getAssetHistGuid());
        asset.setEventHistory(assetHistoryId);
        return asset;
    }

    private eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset mapFromAsset(Asset a) {
        eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset asset = new eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset();
        asset.setCfr(a.getCfr());
        asset.setIrcs(a.getIrcs());
        asset.setIccat(a.getIccat());
        asset.setUvi(a.getUvi());
        asset.setGfcm(a.getGfcm());
        asset.setExternalMarking(a.getExternalMarking());
        asset.setName(a.getName());
        asset.setCountryCode(a.getCountryCode());
        asset.setLengthOverall(Optional.ofNullable(a.getLengthOverAll()).map(BigDecimal::doubleValue).orElse(null));
        asset.setMainGearType(a.getGearType());
        asset.setAssetGuid(a.getAssetId().getGuid());
        asset.setAssetHistGuid(a.getEventHistory().getEventId());
        asset.setAssetHistActive(a.isActive());
        return asset;
    }

    private static Function<Asset, String> toAssetGuid() {
        return (asset) -> asset.getEventHistory().getEventId();
    }

    private static Function<MovementTypeData, String> toMovementGuid() {
        return (mtd) -> mtd.movementType.getGuid();
    }

    private List<MovementMapResponseType> getMovementMapResponseTypes(FilterProcessor processor) throws ReportingServiceException {
        try {
            String request = ExtMovementMessageMapper.mapToGetMovementMapByQueryRequest(processor.toMovementQuery());
            String moduleMessage = movementSender.sendModuleMessage(request, receiver.getDestination());
            TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
            if (response != null && !isUserFault(response)) {
                return ExtMovementMessageMapper.mapToMovementMapResponse(response);
            } else {
                throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT");
            }
        } catch (JMSException | ModelMapperException | MovementModelException | MessageException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM MOVEMENT", e);
        }
    }

    private boolean isUserFault(TextMessage message) {
        boolean isErrorResponse = false;
        try {
            UserFault userFault = JAXBMarshaller.unmarshall(message, UserFault.class);
            log.error("UserFault error JMS message received with text: " + userFault.getFault());
            isErrorResponse = true;
        } catch (ReportingModelException e) {
            //do nothing  since it's not a UserFault
        }
        return isErrorResponse;
    }

    private LineString getLineStringGeometryFromWktString(String wkt) {
        try {
            LineString geometry = (LineString) wktReader.read(wkt);
            geometry.setSRID(4326);
            return geometry;
        } catch (ParseException e) {
            log.error("unable to set geometry from wkt string: {}", wkt);
        } catch (NullPointerException e) {
            log.error("Error occurred with wkt string: {}, possibly no coordinates available", wkt, e);
        }
        return null;
    }

    private String calculateNearestPoint(LineString geometry) {
        List<String> nearestPoint = new ArrayList<>();
        if (geometry != null && geometry.getCentroid() != null) {
            if (geometry.isValid()) {
                DistanceOp distanceOp = new DistanceOp(geometry.getCentroid(), geometry);
                Coordinate[] nearestPoints = distanceOp.nearestPoints();
                nearestPoint.add(String.valueOf(nearestPoints[0].x));
                nearestPoint.add(String.valueOf(nearestPoints[0].y));
            } else if (geometryContainsOnlyOnePoint(geometry)) {
                nearestPoint.add(String.valueOf(geometry.getCoordinate().x));
                nearestPoint.add(String.valueOf(geometry.getCoordinate().y));
            }
        }
        return nearestPoint.stream().collect(Collectors.joining(","));
    }

    private boolean geometryContainsOnlyOnePoint(LineString geometry) {
        return BigDecimal.valueOf(geometry.getLength()).compareTo(BigDecimal.valueOf(0)) == 0;
    }

    private String calculateExtent(LineString geometry) {
        List<String> extent = new ArrayList<>();
        if (geometry != null && geometry.getEnvelopeInternal() != null) {
            Envelope internal = geometry.getEnvelopeInternal();
            extent.add(String.valueOf(internal.getMinX()));
            extent.add(String.valueOf(internal.getMinY()));
            extent.add(String.valueOf(internal.getMaxX()));
            extent.add(String.valueOf(internal.getMaxY()));
        }
        return extent.stream().collect(Collectors.joining(","));
    }

    @AllArgsConstructor
    private static class MovementTypeData {
        MovementType movementType;
        Asset asset;
    }
}