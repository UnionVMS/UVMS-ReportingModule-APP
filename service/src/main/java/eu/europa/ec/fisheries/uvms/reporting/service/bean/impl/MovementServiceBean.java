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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Movement;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Segment;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Track;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.MovementMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
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
     * @param movementTypes List of MovementType to be mapped to Movements
     * @throws ReportingServiceException Thrown if error occurs
     */
    public void createMovementsSegmentsAndTracks(List<MovementType> movementTypes) throws ReportingServiceException {
        Map<String,Asset> assetMap = getFromAsset(movementTypes);
        Map<String,MovementTypeData> movementTypeDataMap = movementTypes.stream()
                .map(movementType -> new MovementTypeData(movementType,assetMap.get(movementType.getConnectId())))
                .collect(Collectors.toMap(toConnectId(),Function.identity()));
        movementTypeDataMap.values().forEach(this::createMovement);
        createSegmentsAndTracks(movementTypes,movementTypeDataMap);
    }

    /**
     * Generates Segment and Tracks based on segment id list
     * @param movementTypes The MovementType list
     */
    public void createSegmentsAndTracks(List<MovementType> movementTypes,Map<String,MovementTypeData> movementTypeDataMap){
        List<SegmentIds> segmentIds = movementTypes.stream()
                                .map(toSegmentId())
                                .collect(Collectors.toList());
        List<SegmentAndTrackList> segmentAndTrackList = movementClient.getSegmentsAndTrackBySegmentIds(segmentIds);
        if(segmentAndTrackList != null) {
            segmentAndTrackList.forEach(segmentAndTrackListItem -> {
                MovementTypeData movementTypeData = movementTypeDataMap.get(segmentAndTrackListItem.getMoveGuid());
                segmentAndTrackListItem.getSegmentAndTrackList().forEach(segmentAndTrack -> {
                    createSegment(segmentAndTrack.getSegment(),movementTypeData);
                    createTrack(segmentAndTrack.getTrack(),movementTypeData);
                });
            });
        }
    }
    
    private static Function<MovementType,SegmentIds> toSegmentId(){
        return (mt)-> {
            SegmentIds segmentId = new SegmentIds();
            segmentId.setMoveGuid(mt.getGuid());
            segmentId.getSegmentIds().addAll(mt.getSegmentIds().stream().map(Long::parseLong).collect(Collectors.toList()));
            return segmentId;
        };
    }
    
    private Movement createMovement(MovementTypeData movementTypeData) {
        Asset asset = movementTypeData.asset;
        MovementType movementType = movementTypeData.movementType;
        return movementRepositoryBean.createMovementEntity(movementMapper.toMovement(movementType/*,asset*/));
    }
    private Segment createSegment(MovementSegment movementSegment,MovementTypeData movementTypeData){
        Segment segment = movementMapper.toSegment(movementSegment/*,movementTypeData.asset*/);
        segment.setMovementGuid(movementTypeData.movementType.getGuid());
        return movementRepositoryBean.createSegmentEntity(segment);
    }
    private Track createTrack(MovementTrack movementTrack, MovementTypeData movementTypeData){
        Track track = movementMapper.toTrack(movementTrack/*,movementTypeData.asset*/);
        return movementRepositoryBean.createTrackEntity(track);
    }

    private Map<String, Asset>  getFromAsset(List<MovementType> movementTypes) throws ReportingServiceException {
        AssetListQuery query = new AssetListQuery();
        AssetListCriteria value = new AssetListCriteria();
        List<AssetListCriteriaPair> criteriaPairs = value.getCriterias();
        for(MovementType movementType : movementTypes){
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
        List<Asset> assets = assetServiceBean.getAssets(query);
        return assets.stream().collect(Collectors.toMap(toAssetGuid(), Function.identity()));
    }
    private static Function<Asset,String> toAssetGuid(){
        return (asset) -> asset.getAssetId().getGuid();
    }
    private static Function<MovementTypeData,String> toConnectId(){
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
    
    @AllArgsConstructor
    private static class MovementTypeData{
        MovementType movementType;
        Asset asset;
    }
}