package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelMarshallException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterExpression;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportingJSONMarshaller;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockVesselData;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.util.MockPointsReader;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.geotools.feature.DefaultFeatureCollection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * //TODO create test
 */
@Stateless
@Local(value = VmsService.class)
public class VmsServiceBean implements VmsService {

    @EJB
    private VesselMessageServiceBean vesselModule;

    @EJB
    private ReportDAO reportDAO;

    @EJB
    private MovementMessageServiceBean movementModule;

    private ReportingJSONMarshaller marshaller;

    @PostConstruct
    public void init(){
        marshaller = new ReportingJSONMarshaller();
    }

    @Override
    public VmsDto getVmsDataByReportId(final Long id) {

        VmsDto vmsDto = null;

        try {

            ReportEntity byId = reportDAO.findById(id);
            FilterExpression filter = marshaller.marshall(byId.getFilterExpression(), FilterExpression.class);

            Map<String, Vessel> vesselMapByGuid = vesselModule.getStringVesselMapByGuid(filter.getVessels());

            MovementQuery movementQuery = filter.createMovementQuery(filter);
            List<MovementMapResponseType> mapResponseTypes = movementModule.getMovementMap(movementQuery);

            vmsDto = VmsDto.getVmsDto(vesselMapByGuid, mapResponseTypes);

        } catch (VesselModelMapperException | MessageException | ReportingModelMarshallException | ModelMapperException | JMSException e) {
            e.printStackTrace(); // TODO throw exception
        }

        return vmsDto;
    }

    @Override
    public VmsDto getVmsMockData(Long id) {

        DefaultFeatureCollection movementFeatureCollection = new DefaultFeatureCollection(null, MovementDto.MOVEMENT);
        DefaultFeatureCollection segmentsFeatureCollection = new DefaultFeatureCollection(null, SegmentDto.SEGMENT);
        MockPointsReader mockPointsReader = new MockPointsReader();
        List <MovementSegment> segmentList = mockPointsReader.segmentList;
        List <MovementType> movementList = mockPointsReader.movementTypeList;
        List <MovementTrack> movementTrackList = mockPointsReader.movementTrackList;
        List<Vessel> vesselDtoList = MockVesselData.getVesselDtoList(2);
        List<TrackDto> tracks = new ArrayList<>();
        int trackid = 1;

        for (Vessel vessel : vesselDtoList){
            int r = MockingUtils.randIntOdd(0, movementList.size());
            List<MovementType> subList = movementList.subList(0, r);
            for(MovementType movementType : subList){
                movementFeatureCollection.add(new MovementDto(movementType, vessel).toFeature());
            }
            int i = r / 2;
            List<MovementSegment> segmentList1 = segmentList.subList(0, i);
            for(MovementSegment segment : segmentList1){
                segment.setTrackId(String.valueOf(trackid));
                segmentsFeatureCollection.add(new SegmentDto(segment, vessel).toFeature());
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
        return new VmsDto(movementFeatureCollection, segmentsFeatureCollection, tracks);
    }

}