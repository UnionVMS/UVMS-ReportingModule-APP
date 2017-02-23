package eu.europa.ec.fisheries.uvms.reporting.entities;

import eu.europa.ec.fisheries.uvms.reporting.entities.converter.GroupCriteriaFilterConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.GroupCriteriaFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.enums.GroupCriteriaType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static eu.europa.ec.fisheries.uvms.reporting.entities.FilterType.criteria;

@Entity
@DiscriminatorValue("GROUP_CRITERIA")
@EqualsAndHashCode(callSuper = false, of = {"code", "orderSequence", "values"})
@ToString(callSuper = true)
public class GroupCriteriaFilter extends Filter {

    @Column(name = "ORDER_SEQ")
    private Integer orderSequence;

    @Column(name = "CRI_TYPE")
    @NotNull
    private String code;

    @Column(name = "CRI_VALUE")
    @Convert(converter = GroupCriteriaFilterConverter.class)
    private List<GroupCriteriaType> values;

    public GroupCriteriaFilter() {
        super(criteria);
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitCriteriaFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        GroupCriteriaFilterMapper.INSTANCE.merge((GroupCriteriaFilter) filter, this);
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

    public void setCode(String code) {
        this.code = code;
    }

    public List<GroupCriteriaType> getValues() {
        return values;
    }

    public void setValues(List<GroupCriteriaType> values) {
        this.values = values;
    }

    public String getCode() {
        return code;
    }
}
