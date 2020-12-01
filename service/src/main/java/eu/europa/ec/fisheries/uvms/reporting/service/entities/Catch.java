package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_catch")
@Data
@IdClass(CatchId.class)
@NoArgsConstructor
public class Catch {

    @Id
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Id
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

    @OneToMany
    @JoinTable(name = "activity_catch_location",
            joinColumns = {@JoinColumn(name = "activity_catch_id"), @JoinColumn(name="activity_species_code")},
            inverseJoinColumns = {@JoinColumn(name = "catch_location_id")}
    )
    private Set<CatchLocation> locations;
}

