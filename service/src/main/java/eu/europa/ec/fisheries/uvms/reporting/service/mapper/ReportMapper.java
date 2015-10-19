package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.security.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportMapper {

    private final ObjectFactory factory = new ObjectFactory();
    private final AuditMapper auditMapper = new AuditMapperImpl();
    private final ExecutionLogMapper executionLogMapper = new ExecutionLogMapperImpl() ;
    private final Set<String> features;
    private final boolean filters;
    private final String currentUser;

    @Builder(builderMethodName = "ReportMapperBuilder")
    ReportMapper(boolean filters, Set<String> features, String currentUser){
        this.currentUser = currentUser;
        this.features = features;
        this.filters = filters;
    }

    public ReportDTO reportToReportDTO(final Report report) {
        if (report == null) {
            return null;
        }
        ReportDTO reportDTO = factory.createReportDTO();
        reportDTO.setId(report.getId() );
        reportDTO.setName(report.getName());
        reportDTO.setDescription(report.getDescription());
        reportDTO.setWithMap(report.getWithMap());
        reportDTO.setScopeName(report.getScopeName());
        reportDTO.setAudit(auditMapper.auditToAuditDTO(report.getAudit()));
        reportDTO.setCreatedBy( report.getCreatedBy());
        if (report.getIsDeleted() != null) {
            reportDTO.setDeleted(report.getIsDeleted());
        }
        reportDTO.setExecutionLogs(executionSetToExecutionsDTOSet(report.getExecutionLogs()));
        reportDTO.setDeletedOn(report.getDeletedOn());
        reportDTO.setDeletedBy(report.getDeletedBy());
        reportDTO.setVisibility(report.getVisibility());
        if (filters) {
            reportDTO.setFilters(filterSetToFilterDTOSet(report.getFilters()));
        }
        if (features != null){
            reportDTO.setShareable(isAllowed(
                    AuthorizationCheckUtil.getRequiredFeatureToShareReport(reportDTO, currentUser), features)
            );
            reportDTO.setDeletable(isAllowed(
                    AuthorizationCheckUtil.getRequiredFeatureToDeleteReport(reportDTO, currentUser), features)
            );
            reportDTO.setEditable(isAllowed(
                    AuthorizationCheckUtil.getRequiredFeatureToEditReport(reportDTO, currentUser), features)
            );
        }
        return reportDTO;
    }

    public Report reportDTOToReport(final ReportDTO dto) {
        if (dto == null) {
            return null;
        }
        Report report = factory.createReport();
        report.setId(dto.getId());
        report.setName(dto.getName());
        report.setDescription(dto.getDescription());
        report.setFilters(filterDTOSetToFilterSet(dto.getFilters(), report));
        report.setWithMap(dto.getWithMap());
        report.setScopeName(dto.getScopeName());
        report.setCreatedBy( dto.getCreatedBy() );
        report.setIsDeleted(dto.isDeleted());
        report.setDeletedOn(dto.getDeletedOn());
        report.setDeletedBy(dto.getDeletedBy());
        report.setVisibility(dto.getVisibility());
        return report;
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

    private List<FilterDTO> filterSetToFilterDTOSet(final Set<Filter> filterSet) {
        if (filterSet == null) {
            return null;
        }
        List<FilterDTO> filterDTOSet = new ArrayList<>();
        for ( Filter filter : filterSet ) {
            FilterDTO filterDTO = filter.convertToDTO();
            filterDTO.setType(filter.getType());
            filterDTOSet.add(filterDTO);
        }
        return filterDTOSet;
    }

    private Set<Filter> filterDTOSetToFilterSet(final List<FilterDTO> filterDTOSet, final Report report) {
        if (filterDTOSet == null) {
            return null;
        }
        Set<Filter> filterSet = new HashSet<Filter>();
        for (FilterDTO filterDTO : filterDTOSet) {
            Filter filter = filterDTO.convertToFilter();
            filter.setReport(report);
            filterSet.add(filter);
        }
        return filterSet;
    }

    private boolean isAllowed(final ReportFeatureEnum requiredFeature, final Set<String> grantedFeatures) {
        boolean isAllowed = false;

        if (requiredFeature == null || grantedFeatures.contains(requiredFeature.toString())){
            isAllowed = true;
        }
        return isAllowed;
    }
}

