package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import lombok.Builder;

@JsonTypeName("vessel")
public class VesselFilterDTO extends FilterDTO {

    public static final String NAME = "name";
    public static final String GUID = "guid";

    private String guid;
    private String name;

    @Builder(builderMethodName = "VesselFilterDTOBuilder")
    public VesselFilterDTO(String guid, String name) {
        this.guid = guid;
        this.name = name;
    }

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
    public Filter convertToFilter() {
        return VesselFilterMapper.INSTANCE.vesselFilterDTOToVesselFilter(this);
    }
}
