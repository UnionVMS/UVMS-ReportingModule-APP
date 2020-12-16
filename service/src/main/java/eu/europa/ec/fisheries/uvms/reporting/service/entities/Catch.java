package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_catch")
@Data
@NoArgsConstructor
public class Catch {

    @Id
    @SequenceGenerator(name = "activity_catch_seq", sequenceName = "activity_catch_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_catch_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "species_code")
    private String speciesCode;

    @Column(name = "catch_type")
    private String catchType;

    @Column(name = "weight_measure_unit_code")
    private String weightMeasureUnitCode;

    @Column(name = "weight_measure", precision = 17, scale = 17)
    private Double weightMeasure;

    @Column(name = "quantity", precision = 17, scale = 17)
    private Double quantity;

    @Column(name = "size_class")
    private String sizeClass;

    @Column(name = "size_category")
    private String sizeCategory;

    @Column(name = "cf", precision = 17, scale = 17)
    private Double cf;

    @Column(name = "presentation")
    private String presentation;

    @Column(name = "preservation")
    private String preservation;

    @Column(name = "product_weight_measure_unit_code")
    private String productWeightMeasureUnitCode;

    @Column(name = "product_weight_measure", precision = 17, scale = 17)
    private Double productWeightMeasure;

    @Column(name = "product_quantity", precision = 17, scale = 17)
    private Double productQuantity;

    @OneToMany(mappedBy = "activityCatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CatchLocation> locations;
}

