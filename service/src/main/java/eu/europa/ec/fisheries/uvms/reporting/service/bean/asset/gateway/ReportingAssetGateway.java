package eu.europa.ec.fisheries.uvms.reporting.service.bean.asset.gateway;

import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;

import java.util.List;
import java.util.Set;

public interface ReportingAssetGateway {


    List<Asset> getAssetListByQuery(AssetListQuery query);

    List<Asset> getAssetGroup(Set<AssetGroup> assetGroupQuery);
}
