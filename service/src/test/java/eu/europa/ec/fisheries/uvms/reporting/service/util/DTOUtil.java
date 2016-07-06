/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.ReportDTOBuilder;

public class DTOUtil {

    public static ReportDTO createReport(String name,
                                         String createdBy,
                                         Date date,
                                         String description,
                                         Boolean withMap,
                                         String scopeName,
                                         VisibilityEnum visibilityEnum) {
        ReportDTO entity = ReportDTOBuilder().build();

        entity.setCreatedBy(createdBy);
        entity.setAudit(new AuditDTO(date));
        entity.setDescription(description);
        entity.setName(name);
        entity.setWithMap(withMap);
        entity.setScopeName(scopeName);
        entity.setVisibility(visibilityEnum);

        return entity;
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


    public static Report createRandomReportEntity() {
        Date currentDate = new Date();

        return Report.builder().details(ReportDetails.builder().name("ReportName" + currentDate.getTime())
                .createdBy("georgi").scopeName("123")
                .description("This is a report description created on " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate))
                .withMap(true).build()).audit(new Audit(currentDate)).build();

    }
}