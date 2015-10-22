package eu.europa.ec.fisheries.uvms.reporting.service.bean;

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
import java.util.HashSet;
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
    public com.google.common.collect.ImmutableMap<String, Vessel> getVesselMapByGuid(final FilterProcessor processor) throws ReportingServiceException {

        Set<Vessel> vesselList = new HashSet<>();

        try {

            if (processor.hasVessels()) {
                String vesselListModuleRequest = ExtendedVesselMessageMapper.createVesselListModuleRequest(processor.toVesselListQuery());
                String moduleMessage = vesselSender.sendModuleMessage(vesselListModuleRequest, vesselReceiver.getDestination());
                TextMessage response = vesselReceiver.getMessage(moduleMessage, TextMessage.class);
                List<Vessel> vessels =  ExtendedVesselMessageMapper.mapToVesselListFromResponse(response, moduleMessage);
                if (vessels != null) {
                    vesselList.addAll(vessels);
                }
            }

            if (processor.hasVesselGroups()) {
                String vesselListModuleRequest = ExtendedVesselMessageMapper.createVesselListModuleRequest(processor.getVesselGroupList());
                String moduleMessage = vesselSender.sendModuleMessage(vesselListModuleRequest, vesselReceiver.getDestination());
                TextMessage response = vesselReceiver.getMessage(moduleMessage, TextMessage.class);
                List<Vessel> groupList = ExtendedVesselMessageMapper.mapToVesselListFromResponse(response, moduleMessage);

                if (groupList != null) {
                    vesselList.addAll(groupList);
                }
            }

        } catch (MessageException | VesselModelMapperException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM VESSEL", e);
        }

        return ExtendedVesselMessageMapper.getVesselMapByGuid(vesselList);
    }
}
