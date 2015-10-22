package eu.europa.ec.fisheries.uvms.reporting.message;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.ListVesselResponse;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselFault;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.mock.Mock;
import org.unitils.mock.annotation.Dummy;

import javax.jms.TextMessage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class ExtendedVesselMessageMapperTest extends UnitilsJUnit4 {

    Mock<TextMessage> message;

    @Test
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequest() throws VesselModelMapperException {

        URL url = Resources.getResource("ExtendedVesselMessageMapperTest.vesselListModuleRequest.xml");
        String expected = Resources.toString(url, Charsets.UTF_8).replaceAll("\r", "");

        assertEquals(expected, ExtendedVesselMessageMapper.mapToGetVesselListByQueryRequest(new VesselListQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToGetMovementMapByQueryRequestException() throws VesselModelMapperException {
        ExtendedVesselMessageMapper.mapToGetVesselListByQueryRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToVesselListFromResponseException1() throws VesselModelMapperException {
        ExtendedVesselMessageMapper.mapToVesselListFromResponse(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToVesselListFromResponseException2() throws VesselModelMapperException {
        ExtendedVesselMessageMapper.mapToVesselListFromResponse(mock(TextMessage.class), null);
    }

}
