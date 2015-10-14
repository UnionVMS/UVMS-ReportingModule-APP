package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AreaFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by georgige on 10/13/2015.
 */
@Entity
@DiscriminatorValue("areas")
@EqualsAndHashCode(callSuper = true)
public class AreaFilter extends Filter{

    @Column(name = "area_type")
    private String areaType;

    @Column(name = "area_id")
    private Long areaId;

    @Builder(builderMethodName = "AreaFilterBuilder")
    public AreaFilter(Long id,
                      Long areaId,
                      String areaType) {
        super(FilterType.areas);
        setId(id);
        this.areaId = areaId;
        this.areaType = areaType;
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

    public AreaFilter() {
        super(FilterType.areas);
    }

    @Override
    public FilterDTO convertToDTO() {
        return AreaFilterMapper.INSTANCE.areaFilterToAreaFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {

    }
}
