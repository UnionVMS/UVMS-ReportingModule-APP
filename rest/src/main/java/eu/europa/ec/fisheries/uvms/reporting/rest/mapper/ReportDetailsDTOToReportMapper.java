package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;



import java.io.IOException;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;

@Mapper 
public abstract class ReportDetailsDTOToReportMapper {
 
	public static ReportDetailsDTOToReportMapper INSTANCE = Mappers.getMapper( ReportDetailsDTOToReportMapper.class );
	
	@Mappings( {
		@Mapping(source = "desc", target = "description")
	})
	public abstract Report reportDetailsDtoToReport(ReportDetailsDTO report);
    
    @InheritInverseConfiguration
    @Mappings( {
    	@Mapping(source = "description", target = "desc")
    })
    public abstract ReportDetailsDTO reportToReportDetailsDto(Report report);
    
}
