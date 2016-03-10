package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayersDto implements Comparable<LayersDto> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("serviceLayerId")
    private String serviceLayerId;

    @JsonProperty("subType")
    private String subType;

    @JsonProperty("order")
    private Long order;

    public LayersDto() {}

    public LayersDto(String name, String serviceLayerId, String subType) {
        this.name = name;
        this.serviceLayerId = serviceLayerId;
        this.subType = subType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("serviceLayerId")
    public String getServiceLayerId() {
        return serviceLayerId;
    }

    @JsonProperty("serviceLayerId")
    public void setServiceLayerId(String serviceLayerId) {
        this.serviceLayerId = serviceLayerId;
    }

    @Override
    public String toString() {
        return "ClassPojo [type = " + name + ", serviceLayerId = " + serviceLayerId + "]";
    }

    @JsonProperty("subType")
    public String getSubType() {
        return subType;
    }

    @JsonProperty("subType")
    public void setSubType(String subType) {
        this.subType = subType;
    }

    @JsonProperty("order")
    public Long getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(Long order) {
        this.order = order;
    }

    @Override
    public int compareTo(LayersDto layersDto) {
        if (this.getOrder() == null || layersDto.getOrder() == null) {
            return 1;
        }
        return Long.compare(this.getOrder(), layersDto.getOrder());
    }
}

