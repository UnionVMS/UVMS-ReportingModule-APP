package eu.europa.ec.fisheries.uvms.reporting.message.mapper;

import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;

import javax.jms.TextMessage;
import java.util.*;

public class ExtAssetMessageMapper {

    private ExtAssetMessageMapper(){}

    public static String mapToGetAssetListByQueryRequest(final AssetListQuery query) throws AssetModelMapperException {
        if (query == null){
            throw new IllegalArgumentException("AssetListQuery can not be null.");
        }
        return AssetModuleRequestMapper.createAssetListModuleRequest(query);
    }

    public static List<Asset> mapToAssetListFromResponse(final TextMessage textMessage, final String correlationId) throws AssetModelMapperException {
        if (textMessage == null){
            throw new IllegalArgumentException("TextMessage can not be null.");
        }
        if (correlationId == null){
            throw new IllegalArgumentException("CorrelationId can not be null.");
        }
        return AssetModuleResponseMapper.mapToAssetListFromResponse(textMessage, correlationId);
    }

    public static Map<String, Asset> getAssetMap(Set<Asset> assetList) {

        Map<String, Asset> map = new HashMap<>();
        
        for(Asset asset : assetList){
            map.put(asset.getAssetId().getGuid(), asset);
        }
        return map;

    }

    public static List<AssetListCriteriaPair> assetCriteria(Set<String> guids) {
        List<AssetListCriteriaPair> pairList = new ArrayList<>();

        for(String guid : guids){
            AssetListCriteriaPair criteriaPair = new AssetListCriteriaPair();
            criteriaPair.setKey(ConfigSearchField.GUID);
            criteriaPair.setValue(guid);
            pairList.add(criteriaPair);
        }

        return pairList;
    }

    public static String createAssetListModuleRequest(AssetListQuery query) throws AssetModelMapperException {
        if (query == null){
            throw new IllegalArgumentException("AssetListQuery can not be null.");
        }
        return AssetModuleRequestMapper.createAssetListModuleRequest(query);
    }

    public static String createAssetListModuleRequest(Set<AssetGroup> assetGroup) throws AssetModelMapperException {
        if (assetGroup == null){
            throw new IllegalArgumentException("List<AssetGroup> can not be null.");
        }
        List<AssetGroup> assetGroupList = new ArrayList<>();
        assetGroupList.addAll(assetGroup);
        return AssetModuleRequestMapper.createAssetListModuleRequest(assetGroupList);
    }
}
