package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import lombok.Builder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class VesselFilterDTO extends FilterDTO {

    public static final String NAME = "name";
    public static final String GUID = "guid";
    public static final String VESSELS = "vessels";

    @Size(min = 1, max = 255)
    private String guid;

    @Size(min = 1, max = 255)
    private String name;

    @Builder(builderMethodName = "VesselFilterDTOBuilder")
    public VesselFilterDTO(Long reportId, Long id, String guid, String name) {
        this.guid = guid;
        this.name = name;
        setId(id);
        setReportId(reportId);
        setType(FilterType.vessel);
        validate();
    }

    @Override
    public void validate() {
        Set<ConstraintViolation<VesselFilterDTO>> violations =
                validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
        }
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
