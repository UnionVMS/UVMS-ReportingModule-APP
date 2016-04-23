package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.*;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.wsdl.asset.group.*;
import eu.europa.ec.fisheries.wsdl.asset.types.*;
import lombok.*;
import org.joda.time.*;

import javax.persistence.*;
import javax.validation.*;
import java.io.*;
import java.util.*;

@Entity
@Table(name = "filter", schema = "reporting")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "FILTER_TYPE")
@NamedQueries({
        @NamedQuery(name = Filter.LIST_BY_REPORT_ID, query = "SELECT f FROM Filter f WHERE report.id = :reportId"),
        @NamedQuery(name = Filter.DELETE_BY_ID, query = "DELETE FROM Filter WHERE id = :id")
})
@EqualsAndHashCode(of = {"id"})
public abstract class Filter implements Serializable {

    @Transient
    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static final String REPORT_ID = "report_id";
    public static final String FILTER_ID = "filter_id";

    public static final String LIST_BY_REPORT_ID = "Filter.listByReportId";
    public static final String DELETE_BY_ID = "Filter.deleteById";

    @Transient
    private final FilterType type;

    @Id
    @Column(name = FILTER_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Filter(FilterType type) {
        this.type = type;
    }

    public Filter(FilterType type, Long id, Long reportId) {
        this(type);
        this.id = id;
        this.reportId = reportId;
    }

    public abstract <T> T accept(FilterVisitor<T> visitor);

    @ManyToOne
    @JoinColumn(name = REPORT_ID, nullable = false)
    private Report report;

    @Transient
    private Long reportId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    }
}
