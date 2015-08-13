package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import java.util.Collection;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;

@Mapper
public interface ReportToDTOMapper {
 
	ReportToDTOMapper INSTANCE = Mappers.getMapper( ReportToDTOMapper.class );
	
	Collection<ReportDTO> reportsToReportDtos(Collection<Report> reports);
 
    ReportDTO reportToReportDto(Report report);
    
    @InheritInverseConfiguration
    Collection<Report> reportDtosToReports(Collection<ReportDTO> reports);
    
    @InheritInverseConfiguration
    Report reportDtoToReport(ReportDTO report);
}