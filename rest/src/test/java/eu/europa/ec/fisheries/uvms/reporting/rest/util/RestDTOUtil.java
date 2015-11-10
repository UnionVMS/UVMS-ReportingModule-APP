package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.ReportDTOBuilder;

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