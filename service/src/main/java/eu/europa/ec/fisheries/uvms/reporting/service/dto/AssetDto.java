package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;

@JsonInclude(Include.NON_NULL)
public class AssetDto {

    @JsonUnwrapped
    private VesselId vesselId;
    private String name;
    private String ircs;
    private String cfr;
    private String countryCode;

    public VesselId getVesselId() {
        return vesselId;
    }

    public void setVesselId(VesselId vesselId) {
        this.vesselId = vesselId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIrcs() {
        return ircs;
    }

    public void setIrcs(String ircs) {
        this.ircs = ircs;
    }

    public String getCfr() {
        return cfr;
    }

    public void setCfr(String cfr) {
        this.cfr = cfr;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
