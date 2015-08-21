package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetDto;
import eu.europa.ec.fisheries.wsdl.vessel.types.CarrierSource;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;

public class AssetMapperTest {

    private AssetMapper mapper;
    private Vessel vessel;

    @Before
    public void beforeTest() {
        mapper = new AssetMapperImpl();
        vessel = new Vessel();
        vessel = new Vessel();

        vessel.setCfr("CFR");
        vessel.setCountryCode("SWE");
        vessel.setExternalMarking("MARKING");
        vessel.setGrossTonnage(BigDecimal.valueOf(1.2));
        vessel.setHasIrcs(true);
        vessel.setHasLicense(true);
        vessel.setHomePort("PORT");

        VesselId vesselId = new VesselId();
        vesselId.setValue("200");
        vessel.setVesselId(vesselId);
        vessel.setIrcs("IRCS");
        vessel.setLengthBetweenPerpendiculars(BigDecimal.valueOf(3));
        vessel.setLengthOverAll(BigDecimal.valueOf(10));
        vessel.setName("VESSEL");
        vessel.setOtherGrossTonnage(BigDecimal.valueOf(30));
        vessel.setPowerAux(BigDecimal.valueOf(999));
        vessel.setPowerMain(BigDecimal.valueOf(999));
        vessel.setSafetyGrossTonnage(BigDecimal.valueOf(100));
        vessel.setActive(true);
        vessel.setSource(CarrierSource.LOCAL);
        vessel.setVesselType("VESSEL-TYPE");
    }

    @Test
    public void shouldReturnVesselDto() {

        // when
        AssetDto assetDto = mapper.vesselToAssetDto(vessel);

        // then
        assertEquals("200", assetDto.getVesselId().getValue());
    }

}
