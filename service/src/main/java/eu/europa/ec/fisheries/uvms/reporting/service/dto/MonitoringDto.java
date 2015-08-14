package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.reporting.service.serializer.MovementDtoListSerializer;

import java.util.List;

/**
 * //TODO create test
 */
public class MonitoringDto {

    @JsonSerialize(using = MovementDtoListSerializer.class)
    private List<MovementDto> movements;

    public List<MovementDto> getMovements() {
        return movements;
    }

    public void setMovements(List<MovementDto> movements) {
        this.movements = movements;
    }

}
