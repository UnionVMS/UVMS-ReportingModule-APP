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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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
    @Type(type = "org.hibernate.spatial.GeometryType")
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
}
