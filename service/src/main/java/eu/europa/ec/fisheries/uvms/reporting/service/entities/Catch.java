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

    @Column(name = "gear_code")
    private String gearCode;

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

    @OneToMany(mappedBy = "activityCatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CatchLocation> locations;

    @OneToMany(mappedBy = "activityCatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CatchProcessing> catchProcessingList;
}

