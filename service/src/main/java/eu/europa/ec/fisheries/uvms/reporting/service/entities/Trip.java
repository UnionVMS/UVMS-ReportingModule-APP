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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

import com.vividsolutions.jts.geom.MultiPoint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.opengis.geometry.primitive.Point;

@Entity
@Table(name = "trip")
@Data
@NoArgsConstructor
public class Trip {

    @Id
    @SequenceGenerator(name = "trip_seq", sequenceName = "trip_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_seq")
    private Long id;

    @Column(name = "trip_id")
    private String tripId;

    @Column(name = "trip_id_scheme")
    private String tripIdScheme;

    @Column(name = "first_fishing_activity")
    private String firstFishingActivity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "first_fishing_activity_timestamp")
    private Date firstFishingActivityDate;

    @Column(name = "last_fishing_activity")
    private String lastFishingActivity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_fishing_activity_timestamp")
    private Date lastFishingActivityDate;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "trip_coordinates", columnDefinition = "Geometry")
    private MultiPoint multipointWkt;

    @Column(name = "trip_duration")
    private Double tripDuration;

    @Column(name = "number_of_positions")
    private Integer positionCount;

    @Column(name = "number_of_corrections")
    private Integer numberOfCorrections;

    @Column(name = "cfr")
    private String cfr;

    @Column(name = "ircs")
    private String ircs;

    @Column(name = "name")
    private String name;

    @Column(name = "ext_mark")
    private String externalMakrking;

    @Column(name = "country_code")
    private String countryCode;

}
