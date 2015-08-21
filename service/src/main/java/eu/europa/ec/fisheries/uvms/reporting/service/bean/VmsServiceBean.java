package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.MovementConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.VesselConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.MovementProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.VesselProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.MovementBaseTypeMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.temp.MockMovementData;
import eu.europa.ec.fisheries.uvms.reporting.service.temp.MockVesselData;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.TextMessage;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * //TODO create test
 */
@Stateless
@Local(value = VmsService.class)
public class VmsServiceBean implements VmsService {

    private static final int LIST_SIZE = 100000;

    @Inject
    private MovementBaseTypeMapper movementBaseTypeMapper;

    @Inject
    private AssetMapper assetMapper;

    @EJB
    private VesselProducerBean vesselSender;

    @EJB
    private VesselConsumerBean vesselReceiver;

    @EJB
    private MovementProducerBean movementSender;

    @EJB
    private MovementConsumerBean movementReceiver;

    @Override
    public VmsDto getMonitoringData(final Set<Integer> vesselIds) {

        try {

            VesselListQuery query = new VesselListQuery();
            query.setVesselSearchCriteria(createVesselListCriteria(vesselIds));
            VesselListPagination pagination = new VesselListPagination();
            pagination.setPage(BigInteger.valueOf(1));
            pagination.setListSize(BigInteger.valueOf(LIST_SIZE));
            query.setPagination(pagination);


            String requestString = VesselModuleRequestMapper.createVesselListModuleRequest(query);
            String messageId = vesselSender.sendModuleMessage(requestString, vesselSender.getDestination());
            TextMessage response = vesselReceiver.getMessage(messageId, TextMessage.class);
            List<Vessel> vessels = VesselModuleResponseMapper.mapToVesselListFromResponse(response, messageId);

            throw new NotImplementedException();

        } catch (VesselModelMapperException | MessageException e) {
          e.printStackTrace();
        }

        return null;
    }

    @Override
    public VmsDto getMonitoringMockData(Set<Integer> vesselIds) {

        int totalMovements = 10, totalVessels = 1;
        List<MovementBaseType> movementBaseTypeList = MockMovementData.getDtoList(totalMovements);
        List<Vessel> assetList = MockVesselData.getVesselDtoList(totalVessels);
        Map<AssetDto, List<MovementDto>> movements = new HashMap<>();
        Map<AssetDto, List<SegmentDto>> segments = new HashMap<>();

        for (Vessel vessel : assetList){
            AssetDto assetDto = assetMapper.vesselToAssetDto(vessel);
            List<MovementDto> movementDtos = new ArrayList<>();
            List<SegmentDto> segmentDtos = new ArrayList<>();

            for (MovementBaseType movementBaseType : movementBaseTypeList) {
                MovementDto movementDto = movementBaseTypeMapper.movementBaseTypeToMovementDto(movementBaseType);
                movementDto.setId(String.valueOf(MockingUtils.randInt(0, totalMovements)));
                movementDtos.add(movementDto);

                SegmentDto segmentDto = new SegmentDto();
                Coordinate coordinate = new Coordinate(MockMovementData.getMovementPoint().getLongitude(), MockMovementData.getMovementPoint().getLatitude());
                Coordinate coordinate1 = new Coordinate(MockMovementData.getMovementPoint().getLongitude(), MockMovementData.getMovementPoint().getLatitude());
                Coordinate[] coords = {coordinate, coordinate1};
                LineString lineString = new GeometryFactory().createLineString(coords);
                segmentDto.setGeometry(lineString);
                segmentDto.setAverageSpeed(BigDecimal.valueOf(MockingUtils.randInt(1, 50)));
                segmentDto.setAverageCourse(BigDecimal.valueOf(MockingUtils.randInt(1, 50)));
                segmentDtos.add(segmentDto);
            }

            movements.put(assetDto, movementDtos);
            segments.put(assetDto, segmentDtos);

        }

        VmsDto monitoringDto = new VmsDto();
        monitoringDto.setMovements(movements);
        monitoringDto.setSegments(segments);
        return  monitoringDto;
    }

    private VesselListCriteria createVesselListCriteria(final Set<Integer> vesselIds){

        VesselListCriteria vesselListCriteria = new VesselListCriteria();

        for (Integer next : vesselIds) {
            VesselListCriteriaPair vesselListCriteriaPair = new VesselListCriteriaPair();
            vesselListCriteriaPair.setKey(ConfigSearchField.IRCS);
            vesselListCriteria.getCriterias().add(vesselListCriteriaPair);
            vesselListCriteriaPair.setValue(String.valueOf(next));
        }

        return vesselListCriteria;
    }

}
