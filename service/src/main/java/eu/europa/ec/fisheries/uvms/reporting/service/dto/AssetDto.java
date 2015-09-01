package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import lombok.experimental.Delegate;

@JsonIgnoreProperties({ "vesselId" })
public class AssetDto {

    @Delegate(types = Include.class)
    private Vessel vessel;
    private String color;
    private String guid;

    public AssetDto(Vessel vessel){
        this.vessel = vessel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private interface Include {
        String getCountryCode();
        String getIrcs();
        String getCfr();
        String getName();
        VesselId getVesselId();
    }

    public String getGuid(){
        return vessel.getVesselId().getGuid();
    }
}
