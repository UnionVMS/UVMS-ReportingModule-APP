package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.movement.model.mock.MockData;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals(12.0, movementDto.getGeometry().getCoordinate().y, 0.001);
        assertEquals(69.0, movementDto.getGeometry().getCoordinate().x, 0.001);
        //assertEquals("4c8aaf26-33cb-4c77-a2f6-62feb3d42fa2", movementDto.getConnectId());FIXME don't use mock
        assertEquals(null, movementDto.getPositionTime());

    }
}