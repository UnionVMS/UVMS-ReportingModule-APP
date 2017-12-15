package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.List;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.GroupCriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.GroupCriteriaFilterMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true, of = {"code", "values"})
@Data
public class CriteriaFilterDTO extends FilterDTO implements Comparable<CriteriaFilterDTO>{

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
