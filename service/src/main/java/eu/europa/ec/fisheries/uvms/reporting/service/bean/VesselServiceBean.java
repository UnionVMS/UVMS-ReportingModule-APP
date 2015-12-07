package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@LocalBean
@Stateless
public class VesselServiceBean {

    @EJB
    private VesselModuleSenderBean vesselSender;

    @EJB
    private ReportingModuleReceiverBean receiver;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Map<String, Vessel> getVesselMap(final FilterProcessor processor) throws ReportingServiceException {
        Set<Vessel> vesselList = new HashSet<>();

        try {
            if (processor.hasVessels()) {
                String request = ExtVesselMessageMapper.createVesselListModuleRequest(processor.toVesselListQuery());
                String moduleMessage = vesselSender.sendModuleMessage(request, receiver.getDestination());
                TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
                List<Vessel> vessels = getVessels(moduleMessage, response);
                vesselList.addAll(vessels);
            }

            if (processor.hasVesselGroups()) {
                String request = ExtVesselMessageMapper.createVesselListModuleRequest(processor.getVesselGroupList());
                String moduleMessage = vesselSender.sendModuleMessage(request, receiver.getDestination());
                TextMessage response = receiver.getMessage(moduleMessage, TextMessage.class);
                List<Vessel> groupList = getVessels(moduleMessage, response);
                vesselList.addAll(groupList);
            }


        } catch (MessageException | VesselModelMapperException e) {
            throw new ReportingServiceException("FAILED TO GET DATA FROM VESSEL", e);
        }

        return ExtVesselMessageMapper.getVesselMap(vesselList);
    }

    // UT
    protected List<Vessel> getVessels(String moduleMessage, TextMessage response) throws VesselModelMapperException {
        return ExtVesselMessageMapper.mapToVesselListFromResponse(response, moduleMessage);
    }
}
