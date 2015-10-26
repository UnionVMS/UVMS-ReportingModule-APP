package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
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
