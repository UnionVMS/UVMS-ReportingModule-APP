package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import java.util.Collection;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;

@Mapper (componentModel = "cdi", uses = {ReportExecutionLogMapper.class}, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ReportMapper {
 
	ReportMapper INSTANCE = Mappers.getMapper( ReportMapper.class );
	
	Collection<Report> reportsToReportDtos(Collection<ReportEntity> reports);
	
    Report reportToReportDto(ReportEntity report);
    
    @InheritInverseConfiguration
    ReportEntity reportDtoToReport(Report report);
    
    @InheritInverseConfiguration
    Collection<ReportEntity> reportDtosToReports(Collection<Report> reports);
}