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
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asset")
@Data
@NoArgsConstructor
public class Asset implements Serializable {

    @Id
    @Column(name = "asset_guid")
    private String assetGuid;

    @Column(name = "iccat")
    private String iccat;

    @Column(name = "uvi")
    private String uvi;

    @Column(name = "cfr")
    private String cfr;

    @Column(name = "ircs")
    private String ircs;

    @Column(name = "name")
    private String name;

    @Column(name = "ext_mark")
    private String externalMakrking;

    @Column(name = "gfcm")
    private String gfcm;

    @Column(name = "country_code")
    private String countryCode;

}
