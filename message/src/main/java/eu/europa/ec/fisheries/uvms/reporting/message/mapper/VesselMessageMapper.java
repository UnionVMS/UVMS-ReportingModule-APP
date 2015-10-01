package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMarshallException;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.jms.TextMessage;
import java.util.List;

public interface VesselMessageMapper {

    String mapToGetVesselListByQueryRequest(VesselListQuery query) throws VesselModelMapperException;

    List<Vessel> mapToVesselListFromResponse(TextMessage response, String correlationId) throws VesselModelMapperException;

    String mapToGetVesselListModuleRequestByVesselGroups(List<VesselGroup> vesselGroups) throws VesselModelMapperException;
}


