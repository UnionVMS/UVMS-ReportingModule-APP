package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.ReportExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLogEntity;

//@Mapper (componentModel = "cdi", uses = {ReportMapper.class})
@Mapper
public interface ReportExecutionLogMapper {
 
	ReportExecutionLogMapper INSTANCE = Mappers.getMapper( ReportExecutionLogMapper.class );
	
	Set<ReportExecutionLog> reportsToReportDtos(Set<ReportExecutionLogEntity> logs);
 
	Set<ReportExecutionLogEntity> reportDtosToReports(Set<ReportExecutionLog> logs);

	ReportExecutionLog reportToReportDto(ReportExecutionLogEntity log);
	
	ReportExecutionLogEntity reportToReportDto(ReportExecutionLog log);
}