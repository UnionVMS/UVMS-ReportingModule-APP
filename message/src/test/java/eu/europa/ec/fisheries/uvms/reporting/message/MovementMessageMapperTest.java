package eu.europa.ec.fisheries.uvms.reporting.message;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.MovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.impl.MovementMessageMapperImpl;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class MovementMessageMapperTest {

    private MovementMessageMapper mapper;

    @Before
    public void beforeEachTest() {
        mapper = new MovementMessageMapperImpl();
    }

    @Test
    public void testMapToGetMovementMapByQueryRequest() throws ModelMarshallException {

        mapper = new MovementMessageMapperImpl(){
            protected String getMapToGetMovementMapByQueryRequest(MovementQuery query) throws ModelMarshallException {
                return "test";}
        };

        assertEquals("test", mapper.mapToGetMovementMapByQueryRequest(new MovementQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToGetMovementMapByQueryRequestException() throws ModelMarshallException {
        mapper.mapToGetMovementMapByQueryRequest(null);
    }

    @Test
    public void testMapToMovementMapResponse() throws ModelMapperException, JMSException {

        final List<MovementMapResponseType> list = new ArrayList<>();
        TextMessage mock = mock(TextMessage.class);

        mapper = new MovementMessageMapperImpl(){
            public List<MovementMapResponseType> getMapToMovementMapResponse(TextMessage message) throws ModelMapperException, JMSException {
                return list;}
        };

        assertEquals(list, mapper.mapToMovementMapResponse(mock));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToMovementMapResponseException() throws JMSException, ModelMapperException {
        mapper.mapToMovementMapResponse(null);
    }
}
