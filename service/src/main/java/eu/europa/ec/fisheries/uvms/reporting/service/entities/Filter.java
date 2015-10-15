package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "filter", schema = "reporting")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="FILTER_TYPE")
@NamedQueries({
        @NamedQuery(name = Filter.LIST_BY_ID, query = "SELECT f FROM Filter f WHERE id IN :id"),
})
public abstract class Filter implements Serializable {

    public static final String LIST_BY_ID = "Filter.listById";

    @Id
    @Column(name = "filter_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Transient
    final private FilterType type;

    public Filter(FilterType type) { this.type = type; }

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Transient
    private Long reportId;

    public abstract FilterDTO convertToDTO();

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


}
