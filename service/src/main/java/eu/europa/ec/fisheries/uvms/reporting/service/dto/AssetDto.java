package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import lombok.experimental.Delegate;

public class AssetDto {

    @Delegate(types = Include.class)
    private Vessel vessel;
    private String color;

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
}
