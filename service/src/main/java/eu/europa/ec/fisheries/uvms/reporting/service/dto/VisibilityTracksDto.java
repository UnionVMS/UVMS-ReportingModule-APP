package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by padhyad on 12/22/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilityTracksDto {

    @NotNull
    @JsonProperty("table")
    private VisibilityAttributesDto table;

    public VisibilityTracksDto(){}

    public VisibilityTracksDto(VisibilityAttributesDto table) {
        this.table = table;
    }

    @JsonProperty("table")
    public VisibilityAttributesDto getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(VisibilityAttributesDto table) {
        this.table = table;
    }
}
