package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import lombok.experimental.Delegate;

public class AssetDTO {

    @Delegate(types = Include.class)
    private Vessel vessel;

    public AssetDTO(Vessel vessel){
        this.vessel = vessel;
    }

    private interface Include {
        String getIrcs();
        String getCfr();
        String getName();
        String getCountryCode();
    }

    public String getGuid(){
        return vessel.getVesselId().getGuid();
    }

}
