/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ListValueTypeFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;

import java.util.*;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;

@Entity
@Table(name = "filter")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "FILTER_TYPE")
@NamedQueries({
        @NamedQuery(name = Filter.LIST_BY_REPORT_ID, query = "SELECT f FROM Filter f WHERE report.id = :reportId"),
        @NamedQuery(name = Filter.DELETE_BY_ID, query = "DELETE FROM Filter WHERE id = :id")
})
@EqualsAndHashCode(callSuper = true, exclude = {"report", "type", "validator", "reportId"})
@AttributeOverride(name = "id", column = @Column(name = "filter_id"))
@ToString(callSuper = true, exclude = {"report", "validator", "type", "reportId"})
public abstract class Filter extends BaseEntity {

    public static final String REPORT_ID = "report_id";
    public static final String LIST_BY_REPORT_ID = "Filter.listByReportId";
    public static final String DELETE_BY_ID = "Filter.deleteById";

    @Transient
    private FilterType type;

    @Transient
    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ManyToOne
    @JoinColumn(name = REPORT_ID, nullable = false)
    private Report report;

    @Transient
    private Long reportId;

    public Filter(FilterType type, Long reportId) {
        this(type);
        this.reportId = reportId;
    }

    public Filter(FilterType type) {
        this.type = type;
    }

    protected Filter() {
    }

    public abstract <T> T accept(FilterVisitor<T> visitor);

    protected void validate() {
        Set<ConstraintViolation<Filter>> violations =
                validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    public abstract void merge(Filter filter);

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public FilterType getType() {
        return type;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public abstract Object getUniqKey();

    public List<AssetListCriteriaPair> assetCriteria() {
        return Collections.emptyList();
    }

    public List<ListCriteria> movementListCriteria() {
        return Collections.emptyList();
    }

    public List<AssetGroup> assetGroupCriteria() {
        return Collections.emptyList();
    }

    public List<RangeCriteria> movementRangeCriteria(DateTime now) {
        return Collections.emptyList();
    }

    public AreaIdentifierType getAreaIdentifierType() {
        return new AreaIdentifierType();
    }

    public List<ListValueTypeFilter> getListValueFilters(DateTime now) {
        return Collections.emptyList();
    }

    public List<SingleValueTypeFilter> getSingleValueFilters(DateTime now) {
        return Collections.emptyList();
    }

    public static class FilterToDTOVisitor implements FilterVisitor<FilterDTO> {

        @Override
        public FilterDTO visitVmsTrackFilter(VmsTrackFilter trackFilter) {
            return VmsTrackFilterMapper.INSTANCE.trackFilterToTrackFilterDTO(trackFilter);
        }

        @Override
        public FilterDTO visitVmsSegmentFilter(VmsSegmentFilter segmentFilter) {
            return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterToVmsSegmentFilterDTO(segmentFilter);
        }

        @Override
        public FilterDTO visitVmsPositionFilter(VmsPositionFilter positionFilter) {
            return VmsPositionFilterMapper.INSTANCE.vmsPositionFilterToVmsPositionFilterDTO(positionFilter);
        }

        @Override
        public FilterDTO visitAssetFilter(AssetFilter assetFilter) {
            return AssetFilterMapper.INSTANCE.assetFilterToAssetFilterDTO(assetFilter);
        }

        @Override
        public FilterDTO visitAssetGroupFilter(AssetGroupFilter assetGroupFilter) {
            return AssetGroupFilterMapper.INSTANCE.assetGroupFilterToAssetGroupFilterDTO(assetGroupFilter);
        }

        @Override
        public FilterDTO visitAreaFilter(AreaFilter areaFilter) {
            return AreaFilterMapper.INSTANCE.areaFilterToAreaFilterDTO(areaFilter);
        }

        @Override
        public FilterDTO visitCommonFilter(CommonFilter commonFilter) {
            return CommonFilterMapper.INSTANCE.dateTimeFilterToDateTimeFilterDTO(commonFilter);
        }

        @Override
        public FilterDTO visitFaFilter(FaFilter faFilter) {
            return FaFilterMapper.INSTANCE.faFilterToFaFilterDto(faFilter);
        }

        @Override
        public FilterDTO visitCriteriaFilter(CriteriaFilter criteriaFilter) {
            return CriteriaFilterMapper.INSTANCE.criteriaFilterToCriteriaFilterDTO(criteriaFilter);
        }
    }
}