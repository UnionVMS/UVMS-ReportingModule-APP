package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import lombok.Builder;

@JsonTypeName("vmsposition")
public class VmsPositionFilterDTO extends FilterDTO {

    public static final String MOV_MIN_SPEED = "movMinSpeed";
    public static final String MOV_MAX_SPEED = "movMaxSpeed";
    public static final String MOV_TYPE = "movType";
    public static final String MOV_ACTIVITY = "movActivity";

    @JsonProperty(MOV_MIN_SPEED)
    private String minimumSpeed;

    @JsonProperty(MOV_MAX_SPEED)
    private String maximumSpeed;

    @JsonProperty(MOV_TYPE)
    private MovementTypeType movementType;

    @JsonProperty(MOV_ACTIVITY)
    private MovementActivityTypeType movementActivity;

    @Builder(builderMethodName = "VmsPositionFilterDTOBuilder")
    public VmsPositionFilterDTO(Long reportId, Long id, String minimumSpeed, String maximumSpeed,
                                MovementTypeType movementType, MovementActivityTypeType movementActivity) {
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.movementType = movementType;
        this.movementActivity = movementActivity;
        setReportId(reportId);
        setId(id);
    }

    public String getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(String minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public String getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(String maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    @Override
    public Filter convertToFilter() {
        return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterDTOToVmsPositionFilter(this);
    }

    public MovementTypeType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementTypeType movementType) {
        this.movementType = movementType;
    }

    public MovementActivityTypeType getMovementActivity() {
        return movementActivity;
    }

    public void setMovementActivity(MovementActivityTypeType movementActivity) {
        this.movementActivity = movementActivity;
    }
}
