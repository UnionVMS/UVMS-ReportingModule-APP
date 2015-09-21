package eu.europa.ec.fisheries.uvms.reporting.message.service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.VesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl.VesselMessageMapperImpl;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.*;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;
import static eu.europa.ec.fisheries.uvms.reporting.model.constants.ModuleConstants.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * //TODO create test
 */
@Stateless
@LocalBean
public class VesselMessageServiceBean extends AbstractMessageService {

    private static final int LIST_SIZE = 1000;

    @Resource(mappedName = QUEUE_VESSEL)
    private Queue response;

    @Resource(mappedName = QUEUE_VESSEL_EVENT)
    private Destination request;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private VesselMessageMapper vesselMessageMapper;

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

    @PostConstruct
    public void init(){
        vesselMessageMapper = new VesselMessageMapperImpl();
    }

    public Map<String, Vessel> getStringVesselMapByGuid(List<eu.europa.ec.fisheries.uvms.reporting.model.Vessel> vessels) throws VesselModelMapperException, MessageException {
        VesselListQuery vesselListQuery = createVesselListQuery(vessels);
        List<Vessel> vesselList = getVessels(vesselListQuery);
        return Maps.uniqueIndex(vesselList, new Function<Vessel, String>() {
            public String apply(Vessel from) {
                return from.getVesselId().getGuid();
            }
        });
    }

    public List<Vessel> getVessels(final VesselListQuery vesselListQuery) throws VesselModelMapperException, MessageException {
        String requestString = vesselMessageMapper.mapToGetVesselListByQueryRequest(vesselListQuery);
        String messageId = sendModuleMessage(requestString);
        TextMessage response = getMessage(messageId, TextMessage.class);
        return vesselMessageMapper.mapToVesselListFromResponse(response, messageId);
    }

    // FIXME maybe move this method to an entity or dto
    private VesselListQuery createVesselListQuery(final List<eu.europa.ec.fisheries.uvms.reporting.model.Vessel> filterVessels) {
        VesselListQuery query = new VesselListQuery();
        query.setVesselSearchCriteria(createVesselListCriteria(filterVessels));
        VesselListPagination pagination = new VesselListPagination();
        pagination.setPage(BigInteger.valueOf(1));
        pagination.setListSize(BigInteger.valueOf(LIST_SIZE));
        query.setPagination(pagination);
        return  query;
    }

    // FIXME maybe move this method to an entity or dto
    private VesselListCriteria createVesselListCriteria(final List<eu.europa.ec.fisheries.uvms.reporting.model.Vessel> filterVessels){

        VesselListCriteria vesselListCriteria = new VesselListCriteria();
        vesselListCriteria.setIsDynamic(false);
        for(eu.europa.ec.fisheries.uvms.reporting.model.Vessel vessel : filterVessels){
            VesselListCriteriaPair vesselListCriteriaPair = new VesselListCriteriaPair();
            vesselListCriteriaPair.setKey(ConfigSearchField.GUID);
            vesselListCriteria.getCriterias().add(vesselListCriteriaPair);
            vesselListCriteriaPair.setValue(vessel.getGuid());
        }

        return vesselListCriteria;
    }
}
