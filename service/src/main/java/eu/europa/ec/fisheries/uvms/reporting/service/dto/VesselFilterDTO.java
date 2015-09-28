package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterDTOVisitor;

@JsonTypeName("vessel")
public class VesselFilterDTO extends FilterDTO {

    private String guid;
    private String name;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(FilterDTOVisitor<T> visitor) {
        return visitor.visitVesselFilterDTO(this);
    }
}
