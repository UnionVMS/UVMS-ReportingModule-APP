package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.security.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

//TODO implement a unit test
public class ReportMapper {

    private final ObjectFactory factory = new ObjectFactory();
    private final AuditMapper auditMapper = new AuditMapperImpl();
    private final ExecutionLogMapper executionLogMapper = new ExecutionLogMapperImpl() ;
    private final Set<String> features;
    private final boolean filters;
    private String currentUser;

    @Builder(builderMethodName = "reportMapperBuilder")
    ReportMapper(boolean filters, Set<String> features, String currentUser){
        this.currentUser = currentUser;
        this.features = features;
        this.filters = filters;
    }

    public void merge(Report incoming, Report existing){
        existing.setId(incoming.getId());
        existing.setName(incoming.getName());
        existing.setDescription(incoming.getDescription());
        existing.setOutComponents(incoming.getOutComponents());
        existing.setScopeName(incoming.getScopeName());
        existing.setCreatedBy(incoming.getCreatedBy());
        existing.setIsDeleted(incoming.isIsDeleted());
        existing.setDeletedOn(incoming.getDeletedOn());
        existing.setDeletedBy( incoming.getDeletedBy() );
        existing.setVisibility( incoming.getVisibility() );
    }
    public ReportDTO reportToReportDto(final Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDTO reportDTO = ReportDTO.builder().build();

        reportDTO.setId( report.getId() );
        reportDTO.setName( report.getName() );
        reportDTO.setDescription( report.getDescription() );
        reportDTO.setOutComponents(report.getOutComponents());
        reportDTO.setScopeName(report.getScopeName());
        reportDTO.setAudit(auditMapper.auditToAuditDTO(report.getAudit()));
        reportDTO.setCreatedBy( report.getCreatedBy() );
        if ( report.isIsDeleted() != null ) {
            reportDTO.setIsDeleted( report.isIsDeleted() );
        }
        reportDTO.setExecutionLogs( executionSetToExecutionsDTOSet(report.getExecutionLogs()));
        reportDTO.setDeletedOn(report.getDeletedOn());
        reportDTO.setDeletedBy(report.getDeletedBy());
        reportDTO.setVisibility(report.getVisibility());
        if (filters) {
            reportDTO.setFilters( filterSetToFilterDTOSet(report.getFilters()) );//TODO TEst
        }

        if (features != null){
            reportDTO.setShareable(isAllowed(AuthorizationCheckUtil.getRequiredFeatureToShareReport(reportDTO, currentUser), features));
            reportDTO.setDeletable(isAllowed(AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(reportDTO, currentUser), features));
            reportDTO.setEditable(isAllowed(AuthorizationCheckUtil.getRequiredFeatureToEditReport(reportDTO, currentUser), features));
        }

        return reportDTO;
    }

    private Set<ExecutionLogDTO> executionSetToExecutionsDTOSet(final Set<ExecutionLog> set) {
        if ( set == null ) {
            return null;
        }

        Set<ExecutionLogDTO> set_ = new HashSet<>();
        for ( ExecutionLog log : set ) {
            set_.add(executionLogMapper.executionLogFilterToExecutionLogFilterDTO(log));
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
        report_.setFilters(filterDTOSetToFilterSet(report.getFilters(), report_));
        report_.setOutComponents(report.getOutComponents());
        report_.setScopeName(report.getScopeName());
        report_.setCreatedBy( report.getCreatedBy() );
        report_.setIsDeleted(report.getIsDeleted());
        report_.setDeletedOn(report.getDeletedOn());
        report_.setDeletedBy(report.getDeletedBy());
        report_.setVisibility( report.getVisibility() );

        return report_;
    }

    protected Set<FilterDTO> filterSetToFilterDTOSet(Set<Filter> set) {
        if ( set == null ) {
            return null;
        }

        Set<FilterDTO> set_ = new HashSet<>();
        for ( Filter filter : set ) {
            set_.add( filter.convertToDTO() );
        }

        return set_;
    }

    protected Set<Filter> filterDTOSetToFilterSet(Set<FilterDTO> set, final Report report) {
        if ( set == null ) {
            return null;
        }

        Set<Filter> set_ = new HashSet<Filter>();
        for ( FilterDTO filterDTO : set ) {
            Filter filter = filterDTO.convertToFilter();
            filter.setReport(report);
            set_.add( filter );
        }

        return set_;
    }

    private boolean isAllowed(final ReportFeatureEnum requiredFeature, Set<String> grantedFeatures) {
        boolean isAllowed = false;

        if (requiredFeature == null || grantedFeatures.contains(requiredFeature.toString())){
            isAllowed = true;
        }
        return isAllowed;
    }
}

