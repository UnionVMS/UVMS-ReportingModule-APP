/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import lombok.ToString;

@ToString
public class AuditDTO {

    @JsonIgnore
    private Date createdOn;

    public AuditDTO(String createdOnValue) {
        if (createdOnValue != null) {
            createdOn = DateUtils.UI_FORMATTER.parseDateTime(createdOnValue).toDate();
        } else {
            createdOn = DateUtils.nowUTC().toDate();
        }
    }

    public AuditDTO(){
        createdOn = DateUtils.getNowDateUTC();
    }

    @JsonProperty("createdOn")
    public String getCreatedOn() {
        return DateUtils.UI_FORMATTER.print(new org.joda.time.DateTime(createdOn));
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}