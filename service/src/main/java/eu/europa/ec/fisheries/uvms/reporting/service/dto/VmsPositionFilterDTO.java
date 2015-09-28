package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterDTOVisitor;

@JsonTypeName("vmsposition")
public class VmsPositionFilterDTO extends FilterDTO {

    private String minimumSpeed;
    private String maximumSpeed;

    public String getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(String minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public String getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(String maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    @Override
    public <T> T accept(FilterDTOVisitor<T> visitor) {
        return visitor.visitVmsPositionFilterDTO(this);
    }
}
