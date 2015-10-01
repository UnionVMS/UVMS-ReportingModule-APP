package eu.europa.ec.fisheries.uvms.reporting.message.service;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.message.MessageService;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import java.util.List;

public interface VesselMessageService extends MessageService {

    public List<Vessel> getVesselsByVesselListQuery(final VesselListQuery vesselListQuery) throws VesselModelMapperException, MessageException;

    public List<Vessel> getVesselsByVesselGroups(final List<VesselGroup> vesselGroups) throws VesselModelMapperException, MessageException;

    public List<Vessel> getVesselsByRequestString(String requestString) throws MessageException, VesselModelMapperException;

}
