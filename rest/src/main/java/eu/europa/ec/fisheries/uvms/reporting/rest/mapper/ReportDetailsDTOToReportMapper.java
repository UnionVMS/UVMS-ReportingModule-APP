package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;

@Mapper
public interface ReportDetailsDTOToReportMapper {
 
	public static ReportDetailsDTOToReportMapper INSTANCE = Mappers.getMapper( ReportDetailsDTOToReportMapper.class );
 
	@Mapping(source = "desc", target = "description")
	public Report reportDetailsDtoToReport(ReportDetailsDTO report);
    
    @InheritInverseConfiguration
    @Mapping(source = "description", target = "desc")
    public ReportDetailsDTO reportToReportDetailsDto(Report report);
    
}
