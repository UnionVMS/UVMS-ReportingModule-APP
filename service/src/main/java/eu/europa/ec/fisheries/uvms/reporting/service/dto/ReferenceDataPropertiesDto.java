package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by padhyad on 6/1/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceDataPropertiesDto {

    @JsonProperty("selection")
    private String selection;

    @JsonProperty("codes")
    private List<String> codes;

    public ReferenceDataPropertiesDto() {}

    public ReferenceDataPropertiesDto(String selection, List<String> codes) {
        this.selection = selection;
        this.codes = codes;
    }

    @JsonProperty("selection")
    public String getSelection() {
        return selection;
    }

    @JsonProperty("selection")
    public void setSelection(String selection) {
        this.selection = selection;
    }

    @JsonProperty("codes")
    public List<String> getCodes() {
        return codes;
    }

    @JsonProperty("codes")
    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
