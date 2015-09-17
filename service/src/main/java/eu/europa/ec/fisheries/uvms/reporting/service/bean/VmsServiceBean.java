package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.module.v1.GetMovementMapByQueryResponse;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.MovementConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.VesselConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.MovementProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.VesselProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockVesselData;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.util.MockPointsReader;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.*;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.TextMessage;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * //TODO create test
 */
@Stateless
@Local(value = VmsService.class)
public class VmsServiceBean implements VmsService {

    private static final int LIST_SIZE = 1000;

    @EJB
    private VesselProducerBean vesselSender;

    @EJB
    private ReportDAO reportDAO;

    @EJB
    private VesselConsumerBean vesselReceiver;

    @EJB
    private MovementProducerBean movementSender;

    @EJB
    private MovementConsumerBean movementReceiver;

    @Override
    public VmsDto getVmsDataByReportId(final Long id) {
        try {

            final ReportEntity byId = reportDAO.findById(id);
            String filterExpression = byId.getFilterExpression();

            VesselListQuery query = new VesselListQuery();
            query.setVesselSearchCriteria(createVesselListCriteria());
            VesselListPagination pagination = new VesselListPagination();
            pagination.setPage(BigInteger.valueOf(1));
            pagination.setListSize(BigInteger.valueOf(LIST_SIZE));
            query.setPagination(pagination);

            String requestString = VesselModuleRequestMapper.createVesselListModuleRequest(query);
            String messageId = vesselSender.sendModuleMessage(requestString, vesselReceiver.getDestination());
            TextMessage response = vesselReceiver.getMessage(messageId, TextMessage.class);
            List<Vessel> vessels = VesselModuleResponseMapper.mapToVesselListFromResponse(response, messageId);

            MovementQuery movementQuery = new MovementQuery();
            movementQuery.getMovementSearchCriteria().addAll(createMovementListCriteria(vessels));
            String movementRequestString = MovementModuleRequestMapper.mapToGetMovementMapByQueryRequest(movementQuery);

            String movementMessageId = movementSender.sendModuleMessage(movementRequestString, movementReceiver.getDestination());
            GetMovementMapByQueryResponse mapByQueryResponse = movementReceiver.getMessage(movementMessageId, GetMovementMapByQueryResponse.class);

            throw new NotImplementedException("wait a bit");

        } catch (VesselModelMapperException | MessageException | ModelMarshallException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public VmsDto getVmsMockData(Set<String> vesselIds) {

        MockPointsReader mockPointsReader = new MockPointsReader();
        List <MovementSegment> segmentList = mockPointsReader.segmentList;
        List <MovementType> movementList = mockPointsReader.movementTypeList;
        List <MovementTrack> movementTrackList = mockPointsReader.movementTrackList;
        List<Vessel> vesselDtoList = MockVesselData.getVesselDtoList(2);
        List<MovementDto> movements = new ArrayList<>();
        List<SegmentDto> segments = new ArrayList<>();
        List<TrackDto> tracks = new ArrayList<>();
        int trackid = 1;

        for (Vessel vessel : vesselDtoList){
            int r = MockingUtils.randIntOdd(0, movementList.size());
            List<MovementType> subList = movementList.subList(0, r);
            for(MovementType movementType : subList){
                movements.add(new MovementDto(movementType, vessel));
            }
            int i = r / 2;
            List<MovementSegment> segmentList1 = segmentList.subList(0, i);
            for(MovementSegment segment : segmentList1){
                segment.setTrackId(String.valueOf(trackid));
                segments.add(new SegmentDto(segment, vessel));
            }

            List<MovementTrack> movementTracks = movementTrackList.subList(0, vesselDtoList.size());
            for (MovementTrack movementTrack : movementTracks){
                movementTrack.setId(String.valueOf(trackid));
                tracks.add(new TrackDto(movementTrack, vessel));
            }

            movementTrackList.removeAll(movementTracks);
            movementList.removeAll(subList);
            segmentList.removeAll(segmentList1);
        }
        return new VmsDto(movements, segments, tracks);
    }

    private VesselListCriteria createVesselListCriteria(){

        VesselListCriteria vesselListCriteria = new VesselListCriteria();
        vesselListCriteria.setIsDynamic(false);
            VesselListCriteriaPair vesselListCriteriaPair = new VesselListCriteriaPair();
            vesselListCriteriaPair.setKey(ConfigSearchField.CFR);
            vesselListCriteria.getCriterias().add(vesselListCriteriaPair);
            vesselListCriteriaPair.setValue(String.valueOf("SWE000007116"));

        return vesselListCriteria;
    }

    private List<ListCriteria> createMovementListCriteria(List<Vessel> vessels){
        List<ListCriteria> listCriterias = new ArrayList<>();
        return listCriterias;
    }

}
