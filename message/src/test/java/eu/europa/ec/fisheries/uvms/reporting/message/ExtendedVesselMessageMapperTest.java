package eu.europa.ec.fisheries.uvms.reporting.message;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedVesselMessageMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import javax.jms.TextMessage;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class ExtendedVesselMessageMapperTest extends UnitilsJUnit4 {

    @Test
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequest() {

        URL url = Resources.getResource("ExtendedVesselMessageMapperTest.vesselListModuleRequest.xml");
        String expected = Resources.toString(url, Charsets.UTF_8).replaceAll("\r", "");

        assertEquals(expected, ExtendedVesselMessageMapper.mapToGetVesselListByQueryRequest(new VesselListQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequestException() {
        ExtendedVesselMessageMapper.mapToGetVesselListByQueryRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToVesselListFromResponseException1() {
        ExtendedVesselMessageMapper.mapToVesselListFromResponse(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToVesselListFromResponseException2() {
        ExtendedVesselMessageMapper.mapToVesselListFromResponse(mock(TextMessage.class), null);
    }

}
