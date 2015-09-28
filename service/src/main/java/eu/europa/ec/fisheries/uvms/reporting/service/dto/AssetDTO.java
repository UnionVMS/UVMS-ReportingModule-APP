package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import lombok.experimental.Delegate;

@JsonIgnoreProperties({ "vesselId" })
public class AssetDTO {

    @Delegate(types = Include.class)
    private Vessel vessel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String color;
    private String guid;

    public AssetDTO(Vessel vessel){
        this.vessel = vessel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private interface Include {
        String getIrcs();
        String getCfr();
        String getName();
        VesselId getVesselId();
    }

    public String getGuid(){
        return vessel.getVesselId().getGuid();
    }

    @JsonProperty("cc")
    public String getCountryCode(){
        return vessel.getCountryCode();
    }

}
