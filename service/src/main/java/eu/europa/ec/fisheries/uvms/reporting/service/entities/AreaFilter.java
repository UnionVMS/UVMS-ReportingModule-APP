package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.mapper.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.*;

import javax.persistence.*;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.*;

@Entity
@DiscriminatorValue("areas")
@EqualsAndHashCode(callSuper = true, of = {"areaType", "areaId"})
@ToString
public class AreaFilter extends Filter {

    @Column(name = "area_type")
    private String areaType;

    @Column(name = "area_id")
    private Long areaId;

    public AreaFilter() {
        super(areas);
    }

    @Builder
    public AreaFilter(Long id, Long areaId, String areaType) {
        super(areas);
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

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitAreaFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        AreaFilterMapper.INSTANCE.merge((AreaFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

    @Override
    public AreaIdentifierType getAreaIdentifierType() {
        return AreaFilterMapper.INSTANCE.areaIdentifierTypeToAreaFilter(this);
    }

}
