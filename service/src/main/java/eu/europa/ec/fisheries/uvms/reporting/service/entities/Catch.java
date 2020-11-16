package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @Column(name = "weight_measure_unit_code")
    private String weightMeasureUnitCode;

    @Column(name = "weight_measure", precision = 17, scale = 17)
    private Double weightMeasure;

    @Column(name = "quantity", precision = 17, scale = 17)
    private Double quantity;

    @Column(name = "size_class")
    private String sizeClass;

    @Column(name = "presentation")
    private String presentation;

}

