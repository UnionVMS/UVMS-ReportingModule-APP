package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionsDto {

    @NotNull
    @JsonProperty("attribute")
    private String attribute;

    @NotNull
    @JsonProperty("style")
    private Map<String, String> style;

    public PositionsDto() {}

    public PositionsDto(String attribute, Map<String, String> style) {
        this.style = style;
        this.attribute = attribute;
    }

    @JsonProperty("attribute")
    public String getAttribute() {
        return attribute;
    }

    @JsonProperty("attribute")
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @JsonProperty("style")
    public Map<String, String> getStyle() {
        return style;
    }

    @JsonProperty("style")
    public void setStyle(Map<String, String> style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "ClassPojo [style = " + style + ", attribute = " + attribute + "]";
    }
}

