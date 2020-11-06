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

import com.vividsolutions.jts.geom.MultiPoint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.opengis.geometry.primitive.Point;

@Entity
@Table(name = "track")
@Data
@NoArgsConstructor
public class Track {

    @Id
    @SequenceGenerator(name = "track_seq", sequenceName = "track_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "track_seq")
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "nearest_point_coordinates", columnDefinition = "Geometry")
    private Point nearestPoint;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "extent_coordinates", columnDefinition = "Geometry")
    private MultiPoint extent;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "total_time_at_sea")
    private Double totalTimeAtSea;

    @ManyToOne
    @JoinColumn(name = "asset_hist_guid", referencedColumnName = "asset_hist_guid")
    private Asset asset;
}
