package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ObjectFactory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.Feature;
import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeature;

@Mapper(uses = ObjectFactory.class)
public abstract class ReportToDTOMapper {
 
	public static ReportToDTOMapper INSTANCE = Mappers.getMapper( ReportToDTOMapper.class );
	
	public abstract Collection<eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO> reportsToReportDtos(Collection<ReportDTO> reports);
 
	@Mappings({
		@Mapping(source = "description", target = "desc"),
		@Mapping(target = "lastExecTime", expression = "java(mapLastExecutionDate(report))")
	})
	public abstract eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportToReportDto(ReportDTO report);
	
	public Collection<eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO> reportsToReportDtos(Collection<ReportDTO> reports, String username, Context context) {
		List<eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO> reportDTOs  = null;
		
		if (reports != null) {
			reportDTOs = new ArrayList<>(reports.size());
			
			for (ReportDTO report : reports) {
				reportDTOs.add(this.reportToReportDto(report, username, context));
			}
		}
		
		return reportDTOs;
	}
	
	/**
	 * custom mapper
	 * @param report
	 * @return
	 */
	protected Date mapLastExecutionDate(ReportDTO report) {
		Date mappedDate = null;
		
		if (!report.getExecutionLogs().isEmpty()) {
			mappedDate = report.getExecutionLogs().iterator().next().getExecutedOn();
		}
			
		return mappedDate;
	}
	 

	public eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportToReportDto(ReportDTO report, String username, Context context) {
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = this.reportToReportDto(report);
		
		if (!report.getExecutionLogs().isEmpty()) {
			reportDTO.setLastExecTime(report.getExecutionLogs().iterator().next().getExecutedOn());
		}
		
		Set<Feature> grantedFeatures = context.getRole().getFeatures();
		
		if (report!= null && grantedFeatures != null) {
			reportDTO.setShareable(isShareable(report, username, grantedFeatures));
			reportDTO.setDeletable(isDeletable(report, username, grantedFeatures));
			reportDTO.setEditable(isEditable(report, username, grantedFeatures));
		}
		return reportDTO;
	}
    
    private boolean isEditable(ReportDTO report, String username, Set<Feature> grantedFeatures) {
    	boolean isEditable = false;
		
    	if (report.getCreatedBy().equals(username)) {
    		if (grantedFeatures.contains(ReportFeature.MODIFY_PRIVATE_REPORT)) {
    			isEditable = true;
    		}
    	} else if ((report.getVisibility() == VisibilityEnum.SCOPE) && grantedFeatures.contains(ReportFeature.MODIFY_SCOPE_REPORT)) {
    		isEditable = true;
    	} else if ((report.getVisibility() == VisibilityEnum.PUBLIC) && grantedFeatures.contains(ReportFeature.MODIFY_GLOBAL_REPORT)) {
    		isEditable = true;
    	}

    	return isEditable;
	}

	private boolean isDeletable(ReportDTO report, String username, Set<Feature> grantedFeatures) {
		boolean isDeletable = false;
		
		if (report.getCreatedBy().equals(username)) {
			if (grantedFeatures.contains(ReportFeature.DELETE_REPORT)) {
				isDeletable = true;
			}
		} else if ((report.getVisibility() == VisibilityEnum.SCOPE) && grantedFeatures.contains(ReportFeature.DELETE_SCOPE_REPORT)) {
			isDeletable = true;
    	} else if ((report.getVisibility() == VisibilityEnum.PUBLIC) && grantedFeatures.contains(ReportFeature.DELETE_GLOBAL_REPORT)) {
    		isDeletable = true;
    	} 
		
		return isDeletable;
	}

	private boolean isShareable(ReportDTO report, String username, Set<Feature> grantedFeatures) {
		boolean isShareable = false;
		if (report.getCreatedBy().equals(username) ) {
			if (grantedFeatures.contains(ReportFeature.SHARE_REPORTS_SCOPE) || grantedFeatures.contains(ReportFeature.SHARE_REPORTS_GLOBAL)) {
				isShareable = true;
			}
		} else if (grantedFeatures.contains(ReportFeature.SHARE_REPORTS_GLOBAL)) {
			isShareable = true;
		} 
		return isShareable;
	}

	@InheritInverseConfiguration
    public abstract Collection<ReportDTO> reportDtosToReports(Collection<eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO> reports);
    
    @InheritInverseConfiguration
    @Mapping(source = "desc", target = "description")
    public abstract ReportDTO reportDtoToReport(eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO report);
    
}
