package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true, of = {"guid", "name"})
public class VesselFilterDTO extends FilterDTO {

    public static final String NAME = "name";
    public static final String GUID = "guid";
    public static final String VESSELS = "vessels";

    @Size(min = 1, max = 255)
    @NotNull
    private String guid;

    @Size(min = 1, max = 255)
    @NotNull
    private String name;

    public VesselFilterDTO() {
        super(FilterType.vessel);
    }

    public VesselFilterDTO(Long id, Long reportId) {
        super(FilterType.vessel, id, reportId);
    }

    @Builder(builderMethodName = "VesselFilterDTOBuilder")
    public VesselFilterDTO(Long id, Long reportId, String guid, String name) {
        this(id, reportId);
        this.guid = guid;
        this.name = name;
        validate();
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
