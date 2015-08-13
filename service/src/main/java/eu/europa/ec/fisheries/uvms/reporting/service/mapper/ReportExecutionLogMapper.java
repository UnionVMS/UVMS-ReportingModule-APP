package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLogEntity;

@Mapper (uses = {ReportMapper.class})
public interface ReportExecutionLogMapper {
 
	ReportExecutionLogMapper INSTANCE = Mappers.getMapper( ReportExecutionLogMapper.class );
	
	Set<ReportExecutionLog> reportsToReportDtos(Set<ReportExecutionLogEntity> reports);
 
	Set<ReportExecutionLogEntity> reportDtosToReports(Set<ReportExecutionLog> reports);
	
	ReportExecutionLog reportToReportDto(ReportExecutionLogEntity report);
	
	ReportExecutionLogEntity reportToReportDto(ReportExecutionLog report);
}