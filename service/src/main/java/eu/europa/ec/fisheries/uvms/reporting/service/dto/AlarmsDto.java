package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 4/1/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AlarmsDto {

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("open")
    private String open;

    @JsonProperty("closed")
    private String closed;

    @JsonProperty("pending")
    private String pending;

    @JsonProperty("none")
    private String none;

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("open")
    public String getOpen() {
        return open;
    }

    @JsonProperty("open")
    public void setOpen(String open) {
        this.open = open;
    }

    @JsonProperty("closed")
    public String getClosed() {
        return closed;
    }

    @JsonProperty("closed")
    public void setClosed(String closed) {
        this.closed = closed;
    }

    @JsonProperty("pending")
    public String getPending() {
        return pending;
    }

    @JsonProperty("pending")
    public void setPending(String pending) {
        this.pending = pending;
    }

    @JsonProperty("none")
    public String getNone() {
        return none;
    }

    @JsonProperty("none")
    public void setNone(String none) {
        this.none = none;
    }
}
