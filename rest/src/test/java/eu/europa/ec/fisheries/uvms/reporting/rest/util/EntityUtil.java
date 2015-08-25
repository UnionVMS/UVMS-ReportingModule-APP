package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;

public class EntityUtil {

	public static ReportEntity createReportEntity(String name,
													String createdBy,
													Date date,
													String description,
													String filterExpression,
													String outComponents,
													long scopeId) {
		ReportEntity entity = new ReportEntity();
		
		entity.setCreatedBy(createdBy);
		entity.setCreatedOn(date);
		entity.setDescription(description);
		entity.setFilterExpression(filterExpression);
		entity.setName(name);
		entity.setOutComponents(outComponents);
		entity.setReportExecutionLogs(new HashSet());
		entity.setScopeId(scopeId);
		
		return entity;
	}
	
	public static Report createReport(String name,
			String createdBy,
			Date date,
			String description,
			String filterExpression,
			String outComponents,
			long scopeId) {
		Report entity = new Report();
		
		entity.setCreatedBy(createdBy);
		entity.setCreatedOn(date);
		entity.setDescription(description);
		entity.setFilterExpression(filterExpression);
		entity.setName(name);
		entity.setOutComponents(outComponents);
		entity.setScopeId(scopeId);
		entity.setReportExecutionLogs(new HashSet<ReportExecutionLog>());
		
		return entity;
	}
	
	public static ReportEntity createRandomReportEntity() {
		Date currentDate = new Date();
		
		return createReportEntity("ReportName" + currentDate.getTime(),
								"georgi",
								currentDate,
								"This is a report description created on " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate),
								"some filter expression",
								"OutComponents",
								123);
	}
	
	public static Report createRandomReport() {
		Date currentDate = new Date();
		
		return createReport("ReportName" + currentDate.getTime(),
								"georgi",
								currentDate,
								"This is a report description created on " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate),
								"some filter expression",
								"OutComponents",
								123);
	}
	
	public static ReportDetailsDTO createRandomReportDetailsDTO() {
		Date currentDate = new Date();
		ReportDetailsDTO reportDTO =  new ReportDetailsDTO();
		reportDTO.setDesc("This is some bullshit description.");
		reportDTO.setName("NonExisting Report Name " + currentDate.getTime());
		reportDTO.setVisibility(VisibilityEnum.PRIVATE);;
		reportDTO.setScopeId(currentDate.getTime());
		reportDTO.setOutComponents("OutComponents");
		
		return reportDTO;
	}
}
