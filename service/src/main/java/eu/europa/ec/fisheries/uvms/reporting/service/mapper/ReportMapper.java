package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.Feature;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeature;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

public class ReportMapper {

    private final ObjectFactory factory = new ObjectFactory();
    private final FilterMapper filterMapper =  new FilterMapper();
    private final AuditMapper auditMapper = new AuditMapperImpl();
    private final ExecutionLogFilterMapper executionLogFilterMapper = new ExecutionLogFilterMapperImpl() ;
    private final Set<Feature> features;
    private final boolean filters;
    private String currentUser;

    @Builder(builderMethodName = "reportMapperBuilder")
    ReportMapper(boolean filters, Set<Feature> features, String currentUser){
        this.currentUser = currentUser;
        this.features = features;
        this.filters = filters;
    }

    private boolean isShareable(final String username, final Set<Feature> grantedFeatures, final Report report) {
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

    private boolean isEditable(Report report, String username, Set<Feature> grantedFeatures) {
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

    private boolean isDeletable(Report report, String username, Set<Feature> grantedFeatures) {
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

    public Report merge(Report report, final ReportDTO dto) {
        report.setId(dto.getId());
        report.setName(dto.getName());
        report.setDescription(dto.getDescription());
        report.setOutComponents(dto.getOutComponents());
        report.setScopeId(dto.getScopeId());
        report.setCreatedBy( dto.getCreatedBy() );
        if ( report.isIsDeleted() != null ) {
            report.setIsDeleted( report.isIsDeleted() );
        }
        report.setDeletedOn( dto.getDeletedOn() );
        report.setDeletedBy( dto.getDeletedBy() );
        report.setVisibility( dto.getVisibility() );
        if (filters){ // FIXME merge filters?
            report.setFilters( filterDTOSetToFilterSet( dto.getFilters() , report) );
        }

        return report;
    }

    public ReportDTO reportToReportDto(final Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDTO reportDTO = ReportDTO.builder().build();

        if (features != null){
            reportDTO.setShareable(isShareable(currentUser, features, report)); //FIXME username
            reportDTO.setDeletable(isDeletable(report, currentUser, features)); //FIXME username
            reportDTO.setEditable(isEditable(report, currentUser, features)); //FIXME username
        }

        reportDTO.setId( report.getId() );
        reportDTO.setName( report.getName() );
        reportDTO.setDescription( report.getDescription() );
        reportDTO.setOutComponents( report.getOutComponents() );
        reportDTO.setScopeId( report.getScopeId() );
        reportDTO.setAudit(auditMapper.auditToAuditDTO(report.getAudit()));
        reportDTO.setCreatedBy( report.getCreatedBy() );
        if ( report.isIsDeleted() != null ) {
            reportDTO.setIsDeleted( report.isIsDeleted() );
        }
        reportDTO.setExecutionLogs( executionSetToExecutionsDTOSet(report.getExecutionLogs()));
        reportDTO.setDeletedOn( report.getDeletedOn() );
        reportDTO.setDeletedBy( report.getDeletedBy() );
        reportDTO.setVisibility( report.getVisibility() );
        reportDTO.setFilters( filterSetToFilterDTOSet(report.getFilters()) );//TODO TEst

        return reportDTO;
    }

    private Set<ExecutionLogDTO> executionSetToExecutionsDTOSet(final Set<ExecutionLog> set) {
        if ( set == null ) {
            return null;
        }

        Set<ExecutionLogDTO> set_ = new HashSet<>();
        for ( ExecutionLog log : set ) {
            set_.add( executionLogFilterMapper.executionLogFilterToExecutionLogFilterDTO( log ) );
        }

        return set_;
    }

    public Report reportDtoToReport(ReportDTO report) {
        if ( report == null ) {
            return null;
        }

        Report report_ = factory.createReport();

        report_.setId( report.getId() );
        report_.setName( report.getName() );
        report_.setDescription( report.getDescription() );
        report_.setFilters( filterDTOSetToFilterSet( report.getFilters() , report_) );
        report_.setOutComponents( report.getOutComponents() );
        report_.setScopeId( report.getScopeId() );
        report_.setCreatedBy( report.getCreatedBy() );
        report_.setIsDeleted( report.getIsDeleted() );
        report_.setDeletedOn( report.getDeletedOn() );
        report_.setDeletedBy( report.getDeletedBy() );
        report_.setVisibility( report.getVisibility() );

        return report_;
    }

    protected Set<FilterDTO> filterSetToFilterDTOSet(Set<Filter> set) {
        if ( set == null ) {
            return null;
        }

        Set<FilterDTO> set_ = new HashSet<>();
        for ( Filter filter : set ) {
            set_.add( filterMapper.filtersDTOToFilter( filter ) );
        }

        return set_;
    }

    protected Set<Filter> filterDTOSetToFilterSet(Set<FilterDTO> set, final Report report) {
        if ( set == null ) {
            return null;
        }

        Set<Filter> set_ = new HashSet<Filter>();
        for ( FilterDTO filterDTO : set ) {
            Filter filter = filterMapper.filterToFilterDTO(filterDTO);
            filter.setReport(report);
            set_.add( filter );
        }

        return set_;
    }
}

