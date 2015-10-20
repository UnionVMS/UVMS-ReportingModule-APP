package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VMSSEG")
@EqualsAndHashCode(callSuper = true)
public class VmsSegmentFilter extends Filter {

    @Column(name = "MIN_SPEED")
    private Float minimumSpeed;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed;

    @Column(name = "MIN_DURATION")
    private Float minDuration;

    @Column(name = "MAX_DURATION")
    private Float maxDuration;

    @Column(name = "SEG_CATEGORY")
    private String category;


    VmsSegmentFilter(){
        super(FilterType.vmsseg);
    }

    @Builder(builderMethodName = "VmsSegmentFilterBuilder")
    public VmsSegmentFilter(Long id,
                            Float maxDuration,
                            Float minDuration,
                            Float maximumSpeed,
                            Float minimumSpeed,
                            String category) {
        super(FilterType.vmsseg);
        setId(id);
        this.maxDuration = maxDuration;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
        this.minDuration = minDuration;
        this.category   = category;
    }
    @Override
    public FilterDTO convertToDTO() {
        return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterToVmsSegmentFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsSegmentFilter incoming = (VmsSegmentFilter) filter;
        setMaximumSpeed(incoming.getMaximumSpeed());
        setMinimumSpeed(incoming.getMinimumSpeed());
        setMinDuration(incoming.getMinDuration());
        setMaxDuration(incoming.getMaxDuration());
        setCategory(incoming.getCategory());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public Float getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(Float minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public Float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(Float maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public Float getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Float minDuration) {
        this.minDuration = minDuration;
    }

    public Float getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Float maxDuration) {
        this.maxDuration = maxDuration;
    }
}
