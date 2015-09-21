package eu.europa.ec.fisheries.uvms.reporting.message;

import eu.europa.ec.fisheries.uvms.reporting.message.mapper.VesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl.VesselMessageMapperImpl;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import org.junit.Before;
import org.junit.Test;

import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class VesselMessageMapperTest {

    private VesselMessageMapper mapper;

    @Before
    public void beforeEachTest() {
        mapper = new VesselMessageMapperImpl();
    }

    @Test
    public void testMapToGetMovementMapByQueryRequest() throws VesselModelMapperException {

        mapper = new VesselMessageMapperImpl(){
            protected String getMapToGetVesselListByQueryRequest(VesselListQuery query) throws VesselModelMapperException {
                return "test";}
        };

        assertEquals("test", mapper.mapToGetVesselListByQueryRequest(new VesselListQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToGetMovementMapByQueryRequestException() throws VesselModelMapperException {
        mapper.mapToGetVesselListByQueryRequest(null);
    }

    @Test
    public void testMapToVesselListFromResponse() throws VesselModelMapperException {

        final List<Vessel> list = new ArrayList<>();
        TextMessage mock = mock(TextMessage.class);

        mapper = new VesselMessageMapperImpl(){
            protected List<Vessel> getMapToVesselListFromResponse(TextMessage textMessage, String correlationId) throws VesselModelMapperException {
                return list;}
        };

        assertEquals(list, mapper.mapToVesselListFromResponse(mock, "test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToVesselListFromResponseException1() throws VesselModelMapperException {
        mapper.mapToVesselListFromResponse(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToVesselListFromResponseException2() throws VesselModelMapperException {
        mapper.mapToVesselListFromResponse(mock(TextMessage.class), null);
    }

}
