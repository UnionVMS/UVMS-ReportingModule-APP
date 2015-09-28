package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;

public class EntityUtil {

    public static ReportDTO createReport(String name,
                                         String createdBy,
                                         Date date,
                                         String description,
                                         String outComponents,
                                         long scopeId) {
        ReportDTO entity = ReportDTO.builder().build();

        entity.setCreatedBy(createdBy);
        entity.setDescription(description);
        entity.setName(name);
        entity.setOutComponents(outComponents);
        entity.setScopeId(scopeId);
        entity.setExecutionLogs(new HashSet<ExecutionLogDTO>());

        return entity;
    }

    public static ReportDTO createRandomReport() {
        Date currentDate = new Date();

        return createReport("ReportName" + currentDate.getTime(),
                "georgi",
                currentDate,
                "This is a report description created on " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate),
                "{\"map\":true,\"vms\":true}",
                123);
    }

    public static ReportDetailsDTO createRandomReportDetailsDTO() {
        Date currentDate = new Date();
        ReportDetailsDTO reportDTO =  new ReportDetailsDTO();
        reportDTO.setDesc("This is some bullshit description.");
        reportDTO.setName("NonExisting Report Name " + currentDate.getTime());
        reportDTO.setVisibility(VisibilityEnum.PRIVATE);
        reportDTO.setScopeId(currentDate.getTime());
        reportDTO.setOutComponents("{\"map\":true,\"vms\":true}");
        return reportDTO;
    }
}
