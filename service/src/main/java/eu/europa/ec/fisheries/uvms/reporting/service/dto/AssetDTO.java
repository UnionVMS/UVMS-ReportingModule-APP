package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.experimental.Delegate;

public class AssetDTO {

    @Delegate(types = Include.class)
    private Asset asset;

    public AssetDTO(Asset asset){
        this.asset = asset;
    }

    private interface Include {
        String getIrcs();
        String getCfr();
        String getName();
        String getCountryCode();
        String getExternalMarking();
    }

    public String getGuid(){
        return asset.getAssetId().getGuid();
    }

}
