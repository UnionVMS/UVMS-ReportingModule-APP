package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExtVesselMessageMapper {

    private ExtVesselMessageMapper(){}

    public static String mapToGetVesselListByQueryRequest(final VesselListQuery query) throws VesselModelMapperException {
        if (query == null){
            throw new IllegalArgumentException("VesselListQuery can not be null.");
        }
        return VesselModuleRequestMapper.createVesselListModuleRequest(query);
    }

    public static List<Vessel> mapToVesselListFromResponse(final TextMessage textMessage, final String correlationId) throws VesselModelMapperException {
        if (textMessage == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        if (correlationId == null){
            throw new IllegalArgumentException("CorrelationId can not be null.");
        }
        return VesselModuleResponseMapper.mapToVesselListFromResponse(textMessage, correlationId);
    }

    public static ImmutableMap<String, Vessel> getVesselMap(Set<Vessel> vesselList) {
        return Maps.uniqueIndex(vesselList, new Function<Vessel, String>() {
            public String apply(Vessel from) {
                return from.getVesselId().getGuid();
            }
        });
    }

    public static List<VesselListCriteriaPair> vesselCriteria(Set<String> guids) {
        List<VesselListCriteriaPair> pairList = new ArrayList<>();

        for(String guid : guids){
            VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
            criteriaPair.setKey(ConfigSearchField.GUID);
            criteriaPair.setValue(guid);
            pairList.add(criteriaPair);
        }

        return pairList;
    }

    public static String createVesselListModuleRequest(VesselListQuery query) throws VesselModelMapperException {
        if (query == null){
            throw new IllegalArgumentException("VesselListQuery can not be null.");
        }
        return VesselModuleRequestMapper.createVesselListModuleRequest(query);
    }

    public static String createVesselListModuleRequest(List<VesselGroup> vesselGroup) throws VesselModelMapperException {
        if (vesselGroup == null){
            throw new IllegalArgumentException("List<VesselGroup> can not be null.");
        }
        return VesselModuleRequestMapper.createVesselListModuleRequest(vesselGroup);
    }
}
