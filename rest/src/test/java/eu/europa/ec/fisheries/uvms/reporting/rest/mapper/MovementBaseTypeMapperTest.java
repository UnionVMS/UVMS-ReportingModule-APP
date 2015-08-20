package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.movement.model.mock.MockData;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementBaseTypeMapperTest {
    private MovementBaseType movementBaseType;

    @Before
    public void setup(){
        movementBaseType = MockData.getDtoList(1).get(0);
    }

    @Test
    public void testMovementBaseTypeToMovementDto(){

        MovementDto movementDto = MovementBaseTypeMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseType);

        assertEquals("ENT", movementDto.getMessageType().value());
        assertNotNull(movementDto.getConnectId());
        assertEquals("0", movementDto.getId());
        assertEquals("God like", movementDto.getStatus());
        assertEquals("3.2", movementDto.getCalculatedSpeed().toString());
        assertEquals(12, movementDto.getCourse());
        assertEquals(0, movementDto.getMeasuredSpeed().intValue());
        //assertEquals(12.0, movementDto.g.getLatitude());FIXME
        //assertEquals(69.0, movementDto.getPosition().getLongitude());FIXME
        //assertEquals("ABC-80+", movementDto.getMobileTerminal().getId());FIXME
        assertEquals(null, movementDto.getPositionTime());

    }
}