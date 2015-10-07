package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;

import java.util.Date;

public class AuditDTO {

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdOn;

    public AuditDTO(){

    }

    @Builder(builderMethodName = "AuditDTOBuilder")
    @JsonCreator
    public AuditDTO(@JsonProperty("createdOn") Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    @JsonIgnore
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
