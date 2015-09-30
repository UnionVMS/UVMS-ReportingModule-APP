package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;

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
    public Filter convertToFilter() {
        return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterDTOToVmsPositionFilter(this);
    }
}
