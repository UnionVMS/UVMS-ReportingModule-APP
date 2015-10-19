package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AreaFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * Created by georgige on 10/13/2015.
 */
@EqualsAndHashCode(callSuper = true)
public class AreaFilterDTO extends FilterDTO {

    public static final String JSON_ATTR_AREA_TYPE = "areaType";
    public static final String JSON_ATTR_AREA_ID = "areaId";

    @NotNull
    private String areaType;

    @NotNull
    private Long areaId;

    public AreaFilterDTO () {
        setType(FilterType.areas);
    }


    @Builder(builderMethodName = "AreaFilterDTOBuilder")
    public AreaFilterDTO(Long reportId, Long id,  Long areaId, String areaType) {
        this.areaId = areaId;
        this.areaType = areaType;
        setId(id);
        setReportId(reportId);
        setType(FilterType.areas);
        validate();
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Override
    public Filter convertToFilter() {
        return AreaFilterMapper.INSTANCE.areaFilterDTOToAreaFilter(this);
    }

}
