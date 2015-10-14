package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import lombok.Builder;

import javax.validation.constraints.Size;

public class VesselFilterDTO extends FilterDTO {

    public static final String NAME = "name";
    public static final String GUID = "guid";
    public static final String VESSEL = "vessel";

    @Size(min = 3, max = 3)
    private String guid;
    private String name;

    @Builder(builderMethodName = "VesselFilterDTOBuilder")
    public VesselFilterDTO(Long reportId, Long id, String guid, String name) {
        this.guid = guid;
        this.name = name;
        setId(id);
        setReportId(reportId);
        setType(FilterType.vessel);
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
