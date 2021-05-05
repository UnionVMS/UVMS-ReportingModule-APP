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

import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "movement")
@Data
@NoArgsConstructor
public class Movement {

    @Id
    @SequenceGenerator(name = "movement_seq", sequenceName = "movement_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movement_seq")
    private Long id;

    @NotNull
    @Column(name = "position_coordinates", columnDefinition = "Geometry")
    private Point position;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "position_time")
    private Date positionTime;

    @ManyToOne
    @JoinColumn(name = "connect_id", referencedColumnName = "asset_hist_guid")
    private Asset asset;

    @Column(name = "movement_guid")
    private String movementGuid;

    @Column(name = "source")
    private String source;

    @Column(name = "movement_type")
    private String movementType;

    @Column(name = "movement_activity_type")
    private String movementActivityType;

    @Column(name = "reported_course")
    private Double reportedCourse;

    @Column(name = "reported_speed")
    private Double reportedSpeed;

    @Column(name = "calculated_speed")
    private Double calculatedSpeed;

    @Column(name = "closest_country")
    private String closestCountry;

    @Column(name = "closest_country_distance")
    private Double closestCountryDistance;

    @Column(name = "closest_port")
    private String closestPort;

    @Column(name = "closest_port_distance")
    private Double closestPortDistance;

    @OneToMany
    @JoinTable(name = "movement_area",
            joinColumns = {@JoinColumn(name = "movement_id")},
            inverseJoinColumns = {@JoinColumn(name = "area_id")}
    )
    private Set<Area> areas;
}
