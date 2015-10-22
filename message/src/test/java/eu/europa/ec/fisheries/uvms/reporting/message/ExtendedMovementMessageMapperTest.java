package eu.europa.ec.fisheries.uvms.reporting.message;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtendedMovementMessageMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ExtendedMovementMessageMapperTest {

    @Test
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequest() throws ModelMarshallException {

        URL url = Resources.getResource("ExtendedMovementMessageMapperTests.getMovementMapByQueryRequest.xml");
        String expected = Resources.toString(url, Charsets.UTF_8).replaceAll("\r", "");

        assertEquals(expected, ExtendedMovementMessageMapper.mapToGetMovementMapByQueryRequest(new MovementQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToGetMovementMapByQueryRequestException() throws ModelMarshallException {
//        mapper.mapToGetMovementMapByQueryRequest(null);
    }

    @Test
    public void testMapToMovementMapResponse() throws ModelMapperException, JMSException {

        final List<MovementMapResponseType> list = new ArrayList<>();
        TextMessage mock = mock(TextMessage.class);

//        mapper = new ExtendedMovementMessageMapper(){
//            public List<MovementMapResponseType> getMapToMovementMapResponse(TextMessage message) throws ModelMapperException, JMSException {
//                return list;}
//        };
//
//        assertEquals(list, mapper.mapToMovementMapResponse(mock));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToMovementMapResponseException() throws JMSException, ModelMapperException {
//        mapper.mapToMovementMapResponse(null);
    }
}
