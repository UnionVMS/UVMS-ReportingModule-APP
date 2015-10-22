package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.jms.TextMessage;
import java.util.List;
import java.util.Set;

public class ExtendedVesselMessageMapper extends VesselModuleRequestMapper {

    private ExtendedVesselMessageMapper(){}

    public static String mapToGetVesselListByQueryRequest(final VesselListQuery query) throws VesselModelMapperException {
        if (query == null){
            throw new IllegalArgumentException("VesselListQuery can not be null.");
        }
        return getMapToGetVesselListByQueryRequest(query);
    }

    private static String getMapToGetVesselListByQueryRequest(final VesselListQuery query) throws VesselModelMapperException {
        return createVesselListModuleRequest(query);
    }

    public static List<Vessel> mapToVesselListFromResponse(final TextMessage textMessage, final String correlationId) throws VesselModelMapperException {
        if (textMessage == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        if (correlationId == null){
            throw new IllegalArgumentException("CorrelationId can not be null.");
        }
        return getMapToVesselListFromResponse(textMessage, correlationId);
    }

    public static String mapToGetVesselListModuleRequestByVesselGroups(final List<VesselGroup> vesselGroups) throws VesselModelMapperException {
        if (vesselGroups == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        return getMapToGetVesselListModuleRequestByVesselGroups(vesselGroups);
    }

    private static String getMapToGetVesselListModuleRequestByVesselGroups(List<VesselGroup> vesselGroups) throws VesselModelMapperException {
        return VesselModuleRequestMapper.createVesselListModuleRequest(vesselGroups);
    }

    private static List<Vessel> getMapToVesselListFromResponse(final TextMessage textMessage, final String correlationId) throws VesselModelMapperException {
        return VesselModuleResponseMapper.mapToVesselListFromResponse(textMessage, correlationId);
    }

    public static ImmutableMap<String, Vessel> getVesselMapByGuid(Set<Vessel> vesselList) {
        return Maps.uniqueIndex(vesselList, new Function<Vessel, String>() {
            public String apply(Vessel from) {
                return from.getVesselId().getGuid();
            }
        });
    }
}
