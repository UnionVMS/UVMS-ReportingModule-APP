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

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarm")
@Data
@NoArgsConstructor
public class Alarm {

    @Id
    @Column(name="guid")
    private String guid;
    @Column(name="status")
    private String status;
    @Column(name="asset_hist_guid")
    private String assetHistGuid;
    @Column(name="rule_name")
    private String ruleName;
    @Column(name="movement_guid")
    private String movementGuid;
    @Column(name="open_date")
    private Date openDate;
    @Column(name="updated_date")
    private Date updated;
    @Column(name="updated_by")
    private String updatedBy;

}
