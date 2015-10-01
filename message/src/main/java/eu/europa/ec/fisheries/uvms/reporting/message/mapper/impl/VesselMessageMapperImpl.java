package eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl;

import eu.europa.ec.fisheries.uvms.reporting.message.mapper.VesselMessageMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMarshallException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.jms.TextMessage;
import java.util.List;

public class VesselMessageMapperImpl implements VesselMessageMapper {

    @Override
    public String mapToGetVesselListByQueryRequest(final VesselListQuery query) throws VesselModelMapperException {
        if (query == null){
            throw new IllegalArgumentException("VesselListQuery can not be null.");
        }
        return getMapToGetVesselListByQueryRequest(query);
    }

    protected String getMapToGetVesselListByQueryRequest(final VesselListQuery query) throws VesselModelMapperException {
        return VesselModuleRequestMapper.createVesselListModuleRequest(query);
    }

    @Override
    public List<Vessel> mapToVesselListFromResponse(final TextMessage textMessage, final String correlationId) throws VesselModelMapperException {
        if (textMessage == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        if (correlationId == null){
            throw new IllegalArgumentException("CorrelationId can not be null.");
        }
        return getMapToVesselListFromResponse(textMessage, correlationId);
    }

    @Override
    public String mapToGetVesselListModuleRequestByVesselGroups(final List<VesselGroup> vesselGroups) throws VesselModelMapperException {
        if (vesselGroups == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        return getMapToGetVesselListModuleRequestByVesselGroups(vesselGroups);
    }

    protected String getMapToGetVesselListModuleRequestByVesselGroups(List<VesselGroup> vesselGroups) throws VesselModelMapperException {
        return VesselModuleRequestMapper.createVesselListModuleRequest(vesselGroups);
    }

    protected List<Vessel> getMapToVesselListFromResponse(final TextMessage textMessage, final String correlationId) throws VesselModelMapperException {
        return VesselModuleResponseMapper.mapToVesselListFromResponse(textMessage, correlationId);
    }
}
