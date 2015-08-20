package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.Dependent;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.reporting.model.Feature;
import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.ReportFeature;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;

@Mapper
public abstract class ReportToDTOMapper {
 
	public static ReportToDTOMapper INSTANCE = Mappers.getMapper( ReportToDTOMapper.class );
	
	public abstract Collection<ReportDTO> reportsToReportDtos(Collection<Report> reports);
 
	@Mapping(source = "description", target = "desc")
	public abstract ReportDTO reportToReportDto(Report report);
	
	public Collection<ReportDTO> reportsToReportDtos(Collection<Report> reports, String username, Context context) {
		List<ReportDTO> reportDTOs  = null; 
		
		if (reports != null) {
			reportDTOs = new ArrayList<ReportDTO>(reports.size());
			
			for (Report report : reports) {
				reportDTOs.add(this.reportToReportDto(report, username, context));
			}
		}
		
		return reportDTOs;
	}
	 

	public ReportDTO reportToReportDto(Report report, String username, Context context) {
		ReportDTO reportDTO = this.reportToReportDto(report);
		Set<Feature> grantedFeatures = context.getRole().getFeatures();
		
		if (report!= null && grantedFeatures != null) {
			reportDTO.setShareable(isShareable(report, username, grantedFeatures));
			reportDTO.setDeletable(isDeletable(report, username, grantedFeatures));
			reportDTO.setEditable(isEditable(report, username, grantedFeatures));
		}
		return reportDTO;
	}
    
    private boolean isEditable(Report report, String username, Set<Feature> grantedFeatures) {
    	boolean isEditable = false;
		
    	if (report.getCreatedBy().equals(username)) {
    		if (grantedFeatures.contains(ReportFeature.MODIFY_REPORT)) {
    			isEditable = true;
    		}
    	} else if (report.getIsShared() && grantedFeatures.contains(ReportFeature.MODIFY_SHARED_REPORTS)) {
    		isEditable = true;
    	}

    	return isEditable;
	}

	private boolean isDeletable(Report report, String username, Set<Feature> grantedFeatures) {
		boolean isDeletable = false;
		
		if (report.getCreatedBy().equals(username)) {
			if (grantedFeatures.contains(ReportFeature.DELETE_REPORT)) {
				isDeletable = true;
			}
		} else if (report.getIsShared() && grantedFeatures.contains(ReportFeature.DELETE_SHARED_REPORT)) {
			isDeletable = true;
    	}
		
		return isDeletable;
	}

	private boolean isShareable(Report report, String username, Set<Feature> grantedFeatures) {
		boolean isShareable = false;
		if (report.getCreatedBy().equals(username) && grantedFeatures.contains(ReportFeature.SHARE_REPORTS)) {
			isShareable = true;
		}
		return isShareable;
	}

	@InheritInverseConfiguration
    public abstract Collection<Report> reportDtosToReports(Collection<ReportDTO> reports);
    
    @InheritInverseConfiguration
    @Mapping(source = "desc", target = "description")
    public abstract Report reportDtoToReport(ReportDTO report);
    
}
