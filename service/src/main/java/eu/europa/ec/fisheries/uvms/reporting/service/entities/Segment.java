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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.LineString;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "segment")
@Data
@NoArgsConstructor
public class Segment {

    @Id
    @SequenceGenerator(name = "segment_seq", sequenceName = "segment_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "segment_seq")
    private Long id;

    @Column(name = "movement_guid")
    private String movementGuid;

    @Column(name = "track_id")
    private String trackId;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "segment_coordinates", columnDefinition = "Geometry")
    private LineString segment;

    @Column(name = "segment_category")
    private String segmentCategory;

    @Column(name = "course_over_ground")
    private Double courseOverGround;

    @Column(name = "speed_over_ground")
    private Double speedOverGround;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "distance")
    private Double distance;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_guid", referencedColumnName = "asset_guid")
    private Asset asset;

}
