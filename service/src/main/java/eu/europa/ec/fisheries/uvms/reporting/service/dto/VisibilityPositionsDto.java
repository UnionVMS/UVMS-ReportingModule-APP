package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by padhyad on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilityPositionsDto {

    @NotNull
    @JsonProperty("popup")
    private VisibilityAttributesDto popup;

    @NotNull
    @JsonProperty("labels")
    private VisibilityAttributesDto labels;

    @NotNull
    @JsonProperty("table")
    private VisibilityAttributesDto table;

    public VisibilityPositionsDto() {}

    public VisibilityPositionsDto(VisibilityAttributesDto popup, VisibilityAttributesDto labels, VisibilityAttributesDto table) {
        this.popup = popup;
        this.labels = labels;
        this.table = table;
    }

    @JsonProperty("popup")
    public VisibilityAttributesDto getPopup() {
        return popup;
    }

    @JsonProperty("popup")
    public void setPopup(VisibilityAttributesDto popup) {
        this.popup = popup;
    }

    @JsonProperty("labels")
    public VisibilityAttributesDto getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(VisibilityAttributesDto labels) {
        this.labels = labels;
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
