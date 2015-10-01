package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.VesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl.VesselMessageMapperImpl;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;
import static eu.europa.ec.fisheries.uvms.reporting.model.constants.ModuleConstants.MODULE_NAME;

/**
 * //TODO create test
 */
@Stateless
@Local(value = VesselMessageService.class)
public class VesselMessageServiceBean extends AbstractMessageService implements VesselMessageService {

    @Resource(mappedName = QUEUE_VESSEL)
    private Queue response;

    @Resource(mappedName = QUEUE_VESSEL_EVENT)
    private Destination request;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private VesselMessageMapper mapper;

    @PostConstruct
    public void init(){
        mapper = new VesselMessageMapperImpl();
    }

    @Override
    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    protected Destination getEventDestination() {
        return request;
    }

    @Override
    protected Destination getResponseDestination() {
        return response;
    }

    @Override
    protected String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public List<Vessel> getVesselsByVesselListQuery(final VesselListQuery vesselListQuery)
            throws VesselModelMapperException, MessageException {
        String requestString = mapper.mapToGetVesselListByQueryRequest(vesselListQuery);
        return getVesselsByRequestString(requestString);
    }

    @Override
    public List<Vessel> getVesselsByRequestString(String requestString)
            throws MessageException, VesselModelMapperException {
        String messageId = sendModuleMessage(requestString);
        TextMessage response = getMessage(messageId, TextMessage.class);
        return mapper.mapToVesselListFromResponse(response, messageId);
    }

    @Override
    public List<Vessel> getVesselsByVesselGroups(final List<VesselGroup> vesselGroups)
            throws VesselModelMapperException, MessageException {
        String requestString = mapper.mapToGetVesselListModuleRequestByVesselGroups(vesselGroups);
        return getVesselsByRequestString(requestString);
    }


}
