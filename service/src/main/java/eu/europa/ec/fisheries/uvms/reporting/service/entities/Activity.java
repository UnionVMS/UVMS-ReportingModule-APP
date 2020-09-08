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

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.MultiPoint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
public class Activity implements Serializable {

    @Id
    @SequenceGenerator(name = "activity_seq", sequenceName = "activity_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_seq")
    private Long id;

    @Column(name = "activity_id")
    private String activityId;

    @Column(name = "fa_report_id")
    private String faReportId;

    @Column(name = "trip_id")
    private String tripId;

    @Column(name = "report_type")
    private String reportType;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "purpose_code")
    private String purposeCode;

    @Column(name = "source")
    private String source;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accepted_timestamp")
    private Date acceptedDate;

    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "activity_coordinates", columnDefinition = "Geometry")
    private MultiPoint activityCoordinates;

    @Column(name = "gfcm")
    private String gfcm;

    @ElementCollection
    @CollectionTable(name = "activity_species", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "species_code")
    private Set<String> species;

    @ElementCollection
    @CollectionTable(name = "activity_gear", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "gear_code")
    private Set<String> gears;

    @ElementCollection
    @CollectionTable(name = "activity_port", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "port_code")
    private Set<String> ports;

    @OneToMany
    @JoinTable(name = "activity_area",
            joinColumns = {@JoinColumn(name = "activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "area_id")}
    )
    private List<Area> areas;

    @Column(name = "correction")
    private Boolean correction;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_guid", referencedColumnName = "asset_guid")
    private Asset asset;

}
