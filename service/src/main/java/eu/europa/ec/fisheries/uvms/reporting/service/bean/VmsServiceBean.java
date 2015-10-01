package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementMessageService;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselMessageService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsQueryMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockVesselData;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.util.MockPointsReader;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.geotools.feature.DefaultFeatureCollection;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
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
    private VesselMessageService vesselModule;

    @EJB
    private ReportRepository repository;

    @EJB
    private MovementMessageService movementModule;

    @Override
    @Transactional
    public VmsDTO getVmsDataByReportId(final String username, final Long id) throws ServiceException {

        VmsDTO vmsDto = null;

        try {

            Report reportByReportId = repository.findReportByReportId(id);
            VmsQueryMapper mapper = VmsQueryMapper.buildVmsQuery().filters(reportByReportId.getFilters()).build();

            if(mapper.hasVesselsOrVesselGroups()){

                Set<Vessel> vesselList = new HashSet<>();
                if (mapper.getVesselListCriteriaPairs() != null) {
                    List<Vessel> vesselsByVesselListQuery = vesselModule.getVesselsByVesselListQuery(mapper.getVesselListQuery());
                    if (vesselsByVesselListQuery != null) {
                        vesselList.addAll(vesselsByVesselListQuery);
                    }
                }

                if (mapper.getVesselGroupList() != null) {
                    List<Vessel> vesselsByVesselGroups = vesselModule.getVesselsByVesselGroups(mapper.getVesselGroupList());
                    if (vesselsByVesselGroups != null) {
                        vesselList.addAll(vesselsByVesselGroups);
                    }
                }

                ImmutableMap<String, Vessel> stringVesselMapByGuid = getStringVesselMapByGuid(vesselList);

                List<MovementMapResponseType> movementMap = movementModule.getMovementMap(mapper.getMovementQuery());

                vmsDto = VmsDTO.getVmsDto(stringVesselMapByGuid, movementMap);
                reportByReportId.updateExecutionLog(username);
            }

        } catch (VesselModelMapperException | MessageException  e) {
            throw new ServiceException("", e);
        } catch (ModelMapperException | JMSException e) {
            e.printStackTrace();
        }

        return vmsDto;
    }

    private ImmutableMap<String, Vessel> getStringVesselMapByGuid(Set<Vessel> vesselList) {
        return Maps.uniqueIndex(vesselList, new Function<Vessel, String>() {
            public String apply(Vessel from) {
                return from.getVesselId().getGuid();
            }
        });
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