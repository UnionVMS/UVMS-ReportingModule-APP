package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterVisitor;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "filter", schema = "reporting")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="FILTER_TYPE")
@NamedQueries({
        @NamedQuery(name = Filter.LIST_BY_REPORT_ID, query = "SELECT f FROM Filter f WHERE report.id = :reportId"),
})
@EqualsAndHashCode(exclude = "report")
public abstract class Filter implements Serializable {

    public static final String LIST_BY_REPORT_ID = "Filter.listByReportId";

    @Id
    @Column(name = "filter_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public abstract <T> T accept(FilterVisitor<T> visitor);

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

}
