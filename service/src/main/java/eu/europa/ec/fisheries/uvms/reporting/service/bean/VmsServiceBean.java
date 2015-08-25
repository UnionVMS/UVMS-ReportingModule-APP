package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.MovementConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.VesselConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.MovementProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.VesselProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.temp.MockMovementData;
import eu.europa.ec.fisheries.uvms.reporting.service.temp.MockMovementSegmentData;
import eu.europa.ec.fisheries.uvms.reporting.service.temp.MockVesselData;
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

    private static final int LIST_SIZE = 100000;

    @EJB
    private VesselProducerBean vesselSender;

    @EJB
    private VesselConsumerBean vesselReceiver;

    @EJB
    private MovementProducerBean movementSender;

    @EJB
    private MovementConsumerBean movementReceiver;

    @Override
    public VmsDto getVmsData(final Set<Integer> vesselIds) {

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

            throw new NotImplementedException("");

        } catch (VesselModelMapperException | MessageException e) {
          e.printStackTrace();
        }
        return null;
    }

    @Override
    public VmsDto getVmsMockData(Set<Integer> vesselIds) {

        int totalMovements = 100, totalVessels = 50, totalSegments = 100;
        List<MovementBaseType> movementBaseTypeList = MockMovementData.getDtoList(totalMovements);
        List<MovementSegment> segmentList = MockMovementSegmentData.getDtoList(totalSegments);
        List<Vessel> assetList = MockVesselData.getVesselDtoList(totalVessels);
        List<MovementDto> movements = new ArrayList<>();
        List<SegmentDto> segments = new ArrayList<>();
        for (Vessel vessel : assetList){
            for (int i=0;i< MockingUtils.randInt(1,10); i++){
                movements.add(new MovementDto(movementBaseTypeList.get(MockingUtils.randInt(0, totalMovements-1)), vessel));
            }
            for (int i=0;i< MockingUtils.randInt(1,10); i++){
                segments.add(new SegmentDto(segmentList.get(MockingUtils.randInt(0, totalSegments-1)), vessel));
            }
        }

        return new VmsDto(movements, segments);
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
