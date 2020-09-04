/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_catch_processing")
@Data
@NoArgsConstructor
public class CatchProcessing {

    @Id
    @SequenceGenerator(name = "activity_catch_processing_seq", sequenceName = "activity_catch_processing_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_catch_processing_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name="activity_catch_id", nullable = false)
    private Catch activityCatch;

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

}
