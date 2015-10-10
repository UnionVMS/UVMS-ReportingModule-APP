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

        ReportDTO reportDTO = factory.createReportDTO();
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
            reportDTO.setFilters(filterSetToFilterDTOSet(report.getFilters()));
        }

        if (features != null){
            reportDTO.setShareable(isAllowed(AuthorizationCheckUtil.getRequiredFeatureToShareReport(reportDTO, currentUser), features));
            reportDTO.setDeletable(isAllowed(AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(reportDTO, currentUser), features));
            reportDTO.setEditable(isAllowed(AuthorizationCheckUtil.getRequiredFeatureToEditReport(reportDTO, currentUser), features));
        }

        return reportDTO;
    }

    private Set<ExecutionLogDTO> executionSetToExecutionsDTOSet(final Set<ExecutionLog> executionLogSet) {
        if ( executionLogSet == null ) {
            return null;
        }

        Set<ExecutionLogDTO> executionLogDTOSet = new HashSet<>();
        for ( ExecutionLog log : executionLogSet ) {
            executionLogDTOSet.add(executionLogMapper.executionLogFilterToExecutionLogFilterDTO(log));
        }

        return executionLogDTOSet;
    }

    public Report reportDtoToReport(ReportDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Report report = factory.createReport();

        report.setId( dto.getId() );
        report.setName( dto.getName() );
        report.setDescription( dto.getDescription() );
        report.setFilters(filterDTOSetToFilterSet(dto.getFilters(), report));
        report.setOutComponents(dto.getOutComponents());
        report.setScopeName(dto.getScopeName());
        report.setCreatedBy( dto.getCreatedBy() );
        report.setIsDeleted(dto.getIsDeleted());
        report.setDeletedOn(dto.getDeletedOn());
        report.setDeletedBy(dto.getDeletedBy());
        report.setVisibility( dto.getVisibility() );

        return report;
    }

    protected Set<FilterDTO> filterSetToFilterDTOSet(Set<Filter> filterSet) {
        if ( filterSet == null ) {
            return null;
        }

        Set<FilterDTO> filterDTOSet = new HashSet<>();
        for ( Filter filter : filterSet ) {
            FilterDTO filterDTO = filter.convertToDTO();
            filterDTO.setType(filter.getType());
            filterDTOSet.add( filterDTO );
        }

        return filterDTOSet;
    }

    protected Set<Filter> filterDTOSetToFilterSet(Set<FilterDTO> filterDTOSet, final Report report) {
        if ( filterDTOSet == null ) {
            return null;
        }

        Set<Filter> filterSet = new HashSet<Filter>();
        for ( FilterDTO filterDTO : filterDTOSet ) {
            Filter filter = filterDTO.convertToFilter();
            filter.setReport(report);
            filterSet.add( filter );
        }

        return filterSet;
    }

    private boolean isAllowed(final ReportFeatureEnum requiredFeature, Set<String> grantedFeatures) {
        boolean isAllowed = false;

        if (requiredFeature == null || grantedFeatures.contains(requiredFeature.toString())){
            isAllowed = true;
        }
        return isAllowed;
    }
}

