package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import javax.ejb.*;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Set;

/**
 * //TODO create unit test
 */
@LocalBean
@Stateless
public class VesselServiceBean {

    @EJB
    private VesselModuleSenderBean vesselSender;

    @EJB
    private VesselModuleReceiverBean vesselReceiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ImmutableMap<String, Vessel> getVesselMap(final FilterProcessor processor) throws ReportingServiceException {
        Set<Vessel> vesselList = Sets.newHashSet();

        try {
            if (processor.hasVessels()) {
                String vesselListModuleRequest = ExtendedVesselMessageMapper.createVesselListModuleRequest(processor.toVesselListQuery());
                String moduleMessage = vesselSender.sendModuleMessage(vesselListModuleRequest, vesselReceiver.getDestination());
                TextMessage response = vesselReceiver.getMessage(moduleMessage, TextMessage.class);
                List<Vessel> vessels = ExtendedVesselMessageMapper.mapToVesselListFromResponse(response, moduleMessage);
                vesselList.addAll(vessels);
            }

            if (processor.hasVesselGroups()) {
                String vesselListModuleRequest = ExtendedVesselMessageMapper.createVesselListModuleRequest(processor.getVesselGroupList());
                String moduleMessage = vesselSender.sendModuleMessage(vesselListModuleRequest, vesselReceiver.getDestination());
                TextMessage response = vesselReceiver.getMessage(moduleMessage, TextMessage.class);
                List<Vessel> groupList = ExtendedVesselMessageMapper.mapToVesselListFromResponse(response, moduleMessage);
                vesselList.addAll(groupList);
            }
        } catch (MessageException | VesselModelMapperException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM VESSEL", e);
        }

        return ExtendedVesselMessageMapper.getVesselMap(vesselList);
    }
}
