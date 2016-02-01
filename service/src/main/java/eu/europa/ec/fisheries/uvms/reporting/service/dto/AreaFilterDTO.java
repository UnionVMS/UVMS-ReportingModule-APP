package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AreaFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * Created by georgige on 10/13/2015.
 */
@EqualsAndHashCode(callSuper = true, of = {"areaType", "areaId"})
public class AreaFilterDTO extends FilterDTO {

    public static final String JSON_ATTR_AREA_TYPE = "areaType";
    public static final String JSON_ATTR_AREA_ID = "gid";

    @NotNull
    private String areaType;

    @NotNull
    @JsonProperty(JSON_ATTR_AREA_ID)
    private Long areaId;

    public AreaFilterDTO() {
        super(FilterType.areas);
    }

    public AreaFilterDTO(Long id, Long reportId) {
        super(FilterType.areas, id, reportId);
    }

    @Builder(builderMethodName = "AreaFilterDTOBuilder")
    public AreaFilterDTO(Long reportId, Long id, Long areaId, String areaType) {
        this(id, reportId);
        this.areaId = areaId;
        this.areaType = areaType;
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
