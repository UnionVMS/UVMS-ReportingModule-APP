package eu.europa.ec.fisheries.uvms.reporting.service.dto.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by padhyad on 3/25/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmMovementList {

    @JsonProperty("movements")
    private List<AlarmMovement> alarmMovementList;

    @JsonProperty("movements")
    public List<AlarmMovement> getAlarmMovementList() {
        return alarmMovementList;
    }

    @JsonProperty("movements")
    public void setAlarmMovementList(List<AlarmMovement> alarmMovementList) {
        this.alarmMovementList = alarmMovementList;
    }
}
