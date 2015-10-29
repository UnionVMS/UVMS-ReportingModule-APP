package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Destination;

@Stateless
@LocalBean
public class VesselModuleSenderBean extends AbstractProducer  {

    @Resource(mappedName = MessageConstants.QUEUE_VESSEL_EVENT)
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }

}
