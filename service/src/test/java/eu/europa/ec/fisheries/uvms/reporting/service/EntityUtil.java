package eu.europa.ec.fisheries.uvms.reporting.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
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
			long scopeId,
			VisibilityEnum visibilityEnum) {
		Report entity = new Report();
		
		entity.setCreatedBy(createdBy);
		entity.setCreatedOn(date);
		entity.setDescription(description);
		entity.setFilterExpression(filterExpression);
		entity.setName(name);
		entity.setOutComponents(outComponents);
		entity.setReportExecutionLogs(new HashSet());
		entity.setScopeId(scopeId);
		entity.setVisibility(visibilityEnum);
		
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
								123,
								VisibilityEnum.SCOPE);
	}
}
