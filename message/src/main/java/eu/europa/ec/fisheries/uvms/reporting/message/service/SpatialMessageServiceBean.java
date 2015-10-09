package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.SpatialMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl.SpatialMessageMapperImpl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.CONNECTION_FACTORY;
import static eu.europa.ec.fisheries.uvms.reporting.model.constants.ModuleConstants.MODULE_NAME;

/**
 * //TODO create test
 */
@Stateless
@Local(SpatialMessageService.class)
public class SpatialMessageServiceBean extends AbstractMessageService implements SpatialMessageService {

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "TODO") //FIXME
    private Queue response;

    @Resource(mappedName = "TODO") // FIXME
    private Destination request;

    private SpatialMessageMapper mapper;

    @PostConstruct
    public void init(){
        mapper = new SpatialMessageMapperImpl();
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
}
