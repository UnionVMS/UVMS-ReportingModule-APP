package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.europa.ec.fisheries.uvms.reporting.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.GroupCriteriaFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.type.GroupCriteriaType;
import java.util.List;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true, of = {"code", "values"})
public class CriteriaFilterDTO extends FilterDTO implements Comparable<CriteriaFilterDTO>{

    @JsonIgnore
    private Integer orderSequence;

    private String code;

    private List<GroupCriteriaType> values;

    public CriteriaFilterDTO() {
        super(FilterType.criteria);
    }

    public CriteriaFilterDTO(String code, List<GroupCriteriaType> values, Integer seq, Long reportId ) {
        super(FilterType.criteria);
        this.code = code;
        this.values = values;
        this.orderSequence = seq;
        this.setReportId(reportId);
    }

    @Override
    public Filter convertToFilter() {
        return GroupCriteriaFilterMapper.INSTANCE.mapCriteriaFilterDTOToCriteriaFilter(this);
    }

    public Integer getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(Integer orderSequence) {
        this.orderSequence = orderSequence;
    }

    public String getCode() {
        return code;
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

    @Override
    public int compareTo(CriteriaFilterDTO o) {
        return this.orderSequence - o.getOrderSequence();
    }
}
