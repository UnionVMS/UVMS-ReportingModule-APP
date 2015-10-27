package eu.europa.ec.fisheries.uvms.reporting.message;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtVesselMessageMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import javax.jms.TextMessage;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class ExtVesselMessageMapperTest extends UnitilsJUnit4 {

    @Test
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequest() {

        URL url = Resources.getResource("ExtendedVesselMessageMapperTest.vesselListModuleRequest.xml");
        String expected = Resources.toString(url, Charsets.UTF_8).replaceAll("\r", "");

        assertEquals(expected, ExtVesselMessageMapper.mapToGetVesselListByQueryRequest(new VesselListQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequestException() {
        ExtVesselMessageMapper.mapToGetVesselListByQueryRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToVesselListFromResponseException1() {
        ExtVesselMessageMapper.mapToVesselListFromResponse(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToVesselListFromResponseException2() {
        ExtVesselMessageMapper.mapToVesselListFromResponse(mock(TextMessage.class), null);
    }

}
