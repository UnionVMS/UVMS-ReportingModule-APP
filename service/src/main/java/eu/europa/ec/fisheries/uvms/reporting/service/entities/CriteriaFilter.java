package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.model.ers.CriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.CriteriaFilterMapper;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.criteria;

@Entity
@DiscriminatorValue("CRITERIA")
@EqualsAndHashCode(callSuper = false, of = {"code", "orderSequence", "value"})
@ToString(callSuper = true)
public class CriteriaFilter extends Filter {

    @Column(name = "ORDER_SEQ")
    private Integer orderSequence;

    @Enumerated(EnumType.STRING)
    @Column(name = "CRI_TYPE")
    @NotNull
    private CriteriaType code;

    @Column(name = "CRI_VALUE")
    private String value;

    public CriteriaFilter() {
        super(criteria);
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitCriteriaFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        CriteriaFilterMapper.INSTANCE.merge((CriteriaFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

    public Integer getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(Integer orderSequence) {
        this.orderSequence = orderSequence;
    }

    public void setCode(CriteriaType code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CriteriaType getCode() {
        return code;
    }

}
