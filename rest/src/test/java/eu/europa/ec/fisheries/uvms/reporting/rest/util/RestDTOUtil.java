package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class RestDTOUtil {

    public static ReportDTO createReport(String name,
                                         String createdBy,
                                         Date date,
                                         String description,
                                         String outComponents,
                                         String scopeName,
                                         VisibilityEnum visibilityEnum) {
        ReportDTO entity = ReportDTO.builder().build();

        entity.setCreatedBy(createdBy);
        entity.setAudit(new AuditDTO(date));
        entity.setDescription(description);
        entity.setName(name);
        entity.setOutComponents(outComponents);
        entity.setExecutionLogs(new HashSet());
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
                "OutComponents",
                "123",
                VisibilityEnum.SCOPE);
    }
}