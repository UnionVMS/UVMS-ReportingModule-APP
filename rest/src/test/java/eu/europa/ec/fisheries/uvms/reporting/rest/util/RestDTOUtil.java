/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import eu.europa.ec.fisheries.uvms.reporting.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.dto.report.ReportDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

import static eu.europa.ec.fisheries.uvms.reporting.dto.report.ReportDTO.ReportDTOBuilder;

public class RestDTOUtil {

    public static ReportDTO createReport(String name,
                                         String createdBy,
                                         Date date,
                                         String description,
                                         Boolean withMap,
                                         String scopeName,
                                         VisibilityEnum visibilityEnum) {
        ReportDTO dto = ReportDTOBuilder().build();

        dto.setCreatedBy(createdBy);
        dto.setAudit(new AuditDTO(date));
        dto.setDescription(description);
        dto.setName(name);
        dto.setWithMap(withMap);
        dto.setScopeName(scopeName);
        dto.setVisibility(visibilityEnum);

        return dto;
    }

    public static ReportDTO createRandomReport() {
        Date currentDate = new Date();

        return createReport("ReportName" + currentDate.getTime(),
                "georgi",
                currentDate,
                "This is a report description created on " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate),
                true,
                "123",
                VisibilityEnum.SCOPE);
    }
}