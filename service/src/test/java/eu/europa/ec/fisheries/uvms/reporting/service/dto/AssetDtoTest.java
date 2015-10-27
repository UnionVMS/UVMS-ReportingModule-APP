package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.wsdl.vessel.types.CarrierSource;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class AssetDtoTest {

    @Test
    public void testDelegation(){

        // given
        Vessel vessel = getVesselDto(1);

        // when
        AssetDTO dto = new AssetDTO(vessel);

        // then
        assertEquals(dto.getCountryCode(), "SWE1");
        assertEquals(dto.getIrcs(), "IRCS-1");
        assertEquals(dto.getName(), "VESSEL-1");
        assertEquals(dto.getCfr(), "CFR1");

    }

    private Vessel getVesselDto(Integer id) {
        Vessel dto = new Vessel();

        dto.setCfr("CFR" + id);
        dto.setCountryCode("SWE" + id);
        dto.setExternalMarking("MARKING" + 1);
        dto.setGrossTonnage(BigDecimal.valueOf(1.2));
        dto.setHasIrcs("true");
        dto.setHasLicense(true);
        dto.setHomePort("PORT" + id);

        VesselId vesselId = new VesselId();
        vesselId.setGuid(id.toString());
        dto.setVesselId(vesselId);
        dto.setIrcs("IRCS-" + id);
        dto.setLengthBetweenPerpendiculars(BigDecimal.valueOf(10));
        dto.setLengthOverAll(BigDecimal.valueOf(20));
        dto.setName("VESSEL-" + id);
        dto.setOtherGrossTonnage(BigDecimal.valueOf(100));
        dto.setPowerAux(BigDecimal.valueOf(1000));
        dto.setPowerMain(BigDecimal.valueOf(50));
        dto.setSafetyGrossTonnage(BigDecimal.valueOf(55));
        dto.setActive(true);

        if (id % 3 == 0) {
            dto.setActive(true);
        }
        if (id % 2 == 0) {
            dto.setSource(CarrierSource.NATIONAL);
            dto.setActive(false);
        }
        if (id % 5 == 0) {
            dto.setSource(CarrierSource.XEU);
            dto.setActive(true);
            //dto.setVesselType("VESSEL-TYPE: " + id);
        }
       // dto.setVesselType("VESSEL-TYPE: " + id);
        return dto;
    }
}
