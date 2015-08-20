package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.MovementConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.consumer.bean.VesselConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.MovementProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.VesselProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MonitoringDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
@Local(value = MonitoringService.class)
public class MonitoringServiceBean implements MonitoringService {

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
    public MonitoringDto getMonitoringData(final Set<Integer> vesselIds) {

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
