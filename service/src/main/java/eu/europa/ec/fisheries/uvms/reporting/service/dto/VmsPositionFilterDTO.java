package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true, of = {"movementType", "movementActivity", "minimumSpeed", "maximumSpeed"})
public class VmsPositionFilterDTO extends FilterDTO {

    public static final String MOV_MIN_SPEED = "movMinSpeed";
    public static final String MOV_MAX_SPEED = "movMaxSpeed";
    public static final String MOV_TYPE = "movType";
    public static final String MOV_ACTIVITY = "movActivity";
    public static final String VMS_POSITION = "vmsposition";

    @JsonProperty(MOV_MIN_SPEED)
    private Float minimumSpeed;

    @JsonProperty(MOV_MAX_SPEED)
    private Float maximumSpeed;

    @JsonProperty(MOV_TYPE)
    private MovementTypeType movementType;

    @JsonProperty(MOV_ACTIVITY)
    private MovementActivityTypeType movementActivity;

    public VmsPositionFilterDTO() {
        super(FilterType.vmspos);
    }

    public VmsPositionFilterDTO(Long id, Long reportId) {
        super(FilterType.vmspos, id, reportId);
    }

    @Builder(builderMethodName = "VmsPositionFilterDTOBuilder")
    public VmsPositionFilterDTO(Long reportId, Long id, Float minimumSpeed, Float maximumSpeed,
                                MovementTypeType movementType, MovementActivityTypeType movementActivity) {
        this(id, reportId);
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.movementType = movementType;
        this.movementActivity = movementActivity;
        validate();
    }

    public Float getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(Float minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public Float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(Float maximumSpeed) {
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
