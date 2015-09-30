package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.VesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl.VesselMessageMapperImpl;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupQueryMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselQueryMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockVesselData;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.util.MockPointsReader;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.*;
import org.geotools.feature.DefaultFeatureCollection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;

/**
 * //TODO create test
 */
@Stateless
@Local(value = VmsService.class)
public class VmsServiceBean implements VmsService {

    private static final int LIST_SIZE = 1000;

    @EJB
    private VesselMessageServiceBean vesselModule;

    @EJB
    private ReportRepository repository;

    @EJB
    private MovementMessageServiceBean movementModule;

    private VesselQueryMapper vesselQueryMapper;
    private VesselGroupQueryMapper vesselGroupQueryMapper;

    @PostConstruct
    public void init(){
        vesselQueryMapper = new VesselQueryMapper();
        vesselGroupQueryMapper = new VesselGroupQueryMapper();
    }

    @Override
    @Transactional
    public VmsDTO getVmsDataByReportId(final String username, final Long id) throws ServiceException {

        VmsDTO vmsDto = null;

        try {

            Report reportByReportId = repository.findReportByReportId(id);
            final Set<Filter> filters = reportByReportId.getFilters();

            for (Filter next : filters) {
                switch (next.getType()) {
                    case VESSEL:
                        vesselQueryMapper.addCriteria(next);
                        break;
                    case VGROUP:
                        vesselGroupQueryMapper.addCriteria(next);
                        break;
                    case DATETIME:
                        break;
                    case VMSPOS:
                        break;
                    default:
                        break;
                }
            }

            VesselListQuery vesselQuery = vesselQueryMapper.getQuery();
            Map<String, Vessel> vesselMapByGuid;
            if (vesselQuery != null){
                vesselMapByGuid = vesselModule.getStringVesselMapByGuid(vesselQueryMapper.getQuery());
            }

            vesselGroupQueryMapper.getQuery();

//            MovementQuery movementQuery = createMovementQuery(positionFilters, vmsPositionFilters);
//            List<MovementMapResponseType> mapResponseTypes = movementModule.getMovementMap(movementQuery);
//
//            vmsDto = VmsDTO.getVmsDto(vesselMapByGuid, mapResponseTypes);

            ExecutionLog executionLogByUser = reportByReportId.getExecutionLogByUser(username);

            if(executionLogByUser != null){
                executionLogByUser.setExecutedOn(new Date());
            }
            else {
                ExecutionLog executionLog = ExecutionLog.builder().executedBy(username).build();
                reportByReportId.getExecutionLogs().add(executionLog);
            }


        } catch (VesselModelMapperException | MessageException  e) {
            throw new ServiceException("", e);
        }

        return vmsDto;
    }

    private MovementQuery createMovementQuery(Set<DateTimeFilter> positionFilters, Set<VmsPositionFilter> vmsPositionFilters) {
        return null;
    }

    @Override
    public VmsDTO getVmsMockData(Long id) {

        DefaultFeatureCollection movementFeatureCollection = new DefaultFeatureCollection(null, MovementDTO.MOVEMENT);
        DefaultFeatureCollection segmentsFeatureCollection = new DefaultFeatureCollection(null, SegmentDTO.SEGMENT);
        MockPointsReader mockPointsReader = new MockPointsReader();
        List <MovementSegment> segmentList = mockPointsReader.segmentList;
        List <MovementType> movementList = mockPointsReader.movementTypeList;
        List <MovementTrack> movementTrackList = mockPointsReader.movementTrackList;
        List<Vessel> vesselDtoList = MockVesselData.getVesselDtoList(2);
        List<TrackDTO> tracks = new ArrayList<>();
        int trackid = 1;

        for (Vessel vessel : vesselDtoList){
            int r = MockingUtils.randIntOdd(0, movementList.size());
            List<MovementType> subList = movementList.subList(0, r);
            for(MovementType movementType : subList){
                movementFeatureCollection.add(new MovementDTO(movementType, vessel).toFeature());
            }
            int i = r / 2;
            List<MovementSegment> segmentList1 = segmentList.subList(0, i);
            for(MovementSegment segment : segmentList1){
                segment.setTrackId(String.valueOf(trackid));
                segmentsFeatureCollection.add(new SegmentDTO(segment, vessel).toFeature());
            }

            List<MovementTrack> movementTracks = movementTrackList.subList(0, vesselDtoList.size());
            for (MovementTrack movementTrack : movementTracks){
                movementTrack.setId(String.valueOf(trackid));
                tracks.add(new TrackDTO(movementTrack, vessel));
            }

            movementTrackList.removeAll(movementTracks);
            movementList.removeAll(subList);
            segmentList.removeAll(segmentList1);
        }
        return new VmsDTO(movementFeatureCollection, segmentsFeatureCollection, tracks);
    }

}