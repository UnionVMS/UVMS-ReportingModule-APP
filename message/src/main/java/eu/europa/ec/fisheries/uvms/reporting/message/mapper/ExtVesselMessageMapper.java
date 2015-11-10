package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import javax.jms.TextMessage;
import java.util.*;

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

    public static Map<String, Vessel> getVesselMap(Set<Vessel> vesselList) {

        Map<String, Vessel> map = new HashMap<>();
        
        for(Vessel vessel : vesselList){
            map.put(vessel.getVesselId().getGuid(), vessel);
        }
        return map;

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

    public static String createVesselListModuleRequest(Set<VesselGroup> vesselGroup) throws VesselModelMapperException {
        if (vesselGroup == null){
            throw new IllegalArgumentException("List<VesselGroup> can not be null.");
        }
        List<VesselGroup> vesselGroupList = new ArrayList<>();
        vesselGroupList.addAll(vesselGroup);
        return VesselModuleRequestMapper.createVesselListModuleRequest(vesselGroupList);
    }
}
