package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.europa.ec.fisheries.uvms.reporting.model.ers.CriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.CriteriaFilterMapper;

public class CriteriaFilterDTO extends FilterDTO implements Comparable<CriteriaFilterDTO> {

    @JsonIgnore
    private Integer orderSequence;

    private CriteriaType code;

    private String value;

    public CriteriaFilterDTO() {
        super(FilterType.criteria);
    }

    public CriteriaFilterDTO(String code, String value, Integer seq, Long reportId ) {
        super(FilterType.criteria);
        this.code = CriteriaType.valueOf(code);
        this.value = value;
        this.orderSequence = seq;
        this.setReportId(reportId);
    }

    @Override
    public Filter convertToFilter() {
        return CriteriaFilterMapper.INSTANCE.criteriaFilterDTOToCriteriaFilter(this);
    }

    public Integer getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(Integer orderSequence) {
        this.orderSequence = orderSequence;
    }

    public CriteriaType getCode() {
        return code;
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

    @Override
    public int compareTo(CriteriaFilterDTO o) {
        return this.orderSequence - o.getOrderSequence();
    }
}
