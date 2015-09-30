package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.message.MessageService;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import java.util.List;
import java.util.Map;

/**
 * //TODO create test
 */
public interface VesselMessgeService extends MessageService {

    public Map<String, Vessel> getStringVesselMapByGuid(VesselListQuery query) throws VesselModelMapperException, MessageException;

    public List<Vessel> getVessels(final VesselListQuery vesselListQuery) throws VesselModelMapperException, MessageException;

}
