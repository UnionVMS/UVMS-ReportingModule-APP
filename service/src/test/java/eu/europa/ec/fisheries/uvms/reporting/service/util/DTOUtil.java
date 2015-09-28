package eu.europa.ec.fisheries.uvms.reporting.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;

public class DTOUtil {

    public static ReportDTO createReport(String name,
                                         String createdBy,
                                         Date date,
                                         String description,
                                         String outComponents,
                                         long scopeId,
                                         VisibilityEnum visibilityEnum) {
        ReportDTO entity = ReportDTO.builder().build();

        entity.setCreatedBy(createdBy);
        entity.setAudit(new AuditDTO());
        entity.getAudit().setCreatedOn(date);
        entity.setDescription(description);
        entity.setName(name);
        entity.setOutComponents(outComponents);
        entity.setExecutionLogs(new HashSet());
        entity.setScopeId(scopeId);
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
                123,
                VisibilityEnum.SCOPE);
    }
}