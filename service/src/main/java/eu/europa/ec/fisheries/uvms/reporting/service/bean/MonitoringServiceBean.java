package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.MonitoringDto;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.math.BigInteger;
import java.util.Set;

/**
 * //TODO create test
 */
@Stateless
@Local(value = MonitoringService.class)
public class MonitoringServiceBean implements MonitoringService {

    private static final int LIST_SIZE = 100000;

    //@Inject MessageProducer vesselModuleMessageProducer;

    //@Inject @eu.europa.ec.fisheries.uvms.spatial.message.constants.Vessel
    //MessageConsumer vesselModuleResponseQ;

    @Override
    public MonitoringDto getMovements(final Set<Integer> vesselIds) {
       // try {
            VesselListQuery query = new VesselListQuery();
            query.setVesselSearchCriteria(createVesselListCriteria(vesselIds));
            VesselListPagination pagination = new VesselListPagination();
            pagination.setPage(BigInteger.valueOf(1));
            pagination.setListSize(BigInteger.valueOf(LIST_SIZE));
            query.setPagination(pagination);
            //String requestString = VesselModuleRequestMapper.createVesselListModuleRequest(query);
            //String messageId = vesselModuleMessageProducer.sendModuleMessage(requestString, ModuleQueue.VESSEL);


       // } catch (MessageException e) {
       //     e.printStackTrace();
       // }

        return null;
    }

    private VesselListCriteria createVesselListCriteria(final Set<Integer> vesselIds){

        VesselListCriteria vesselListCriteria = new VesselListCriteria();

        for (int vesselId : vesselIds){
            VesselListCriteriaPair vesselListCriteriaPair = new VesselListCriteriaPair();
            vesselListCriteriaPair.setKey(ConfigSearchField.IRCS);
            vesselListCriteria.getCriterias().add(vesselListCriteriaPair);
            vesselListCriteriaPair.setValue(String.valueOf(vesselId));
        }

        return vesselListCriteria;
    }

}
