/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.google.common.collect.Sets;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.security.AuthorizationCheckUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ObjectFactory;
import lombok.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class ReportMapper {

    private final ObjectFactory factory = new ObjectFactory();
    private final AuditMapper auditMapper = new AuditMapperImpl();
    private final ExecutionLogMapper executionLogMapper = new ExecutionLogMapperImpl();
    private Set<String> features = null;
    private boolean filters = false;
    private String currentUser = null;

    @Builder(builderMethodName = "ReportMapperBuilder")
    public ReportMapper(boolean filters, Set<String> features, String currentUser) {
        this.currentUser = currentUser;
        this.features = features;
        this.filters = filters;
    }
    public ReportMapper() {

    }

    public ReportDTO reportToReportDTO(final Report report) {
        if (report == null) {
            return null;
        }
        ReportDTO reportDTO = factory.createReportDTO();
        reportDTO.setId(report.getId());
        reportDTO.setName(report.getDetails().getName());
        reportDTO.setDescription(report.getDetails().getDescription());
        reportDTO.setWithMap(report.getDetails().getWithMap());
        reportDTO.setScopeName(report.getDetails().getScopeName());
        reportDTO.setAudit(auditMapper.auditToAuditDTO(report.getAudit()));
        reportDTO.setCreatedBy(report.getDetails().getCreatedBy());
        if (report.getIsDeleted() != null) {
            reportDTO.setDeleted(report.getIsDeleted());
        }
        reportDTO.setExecutionLog(getExecutionLogDTO(report.getExecutionLogs()));
        reportDTO.setDeletedOn(report.getDeletedOn());
        reportDTO.setDeletedBy(report.getDeletedBy());
        reportDTO.setVisibility(report.getVisibility());
        reportDTO.setReportTypeEnum(report.getReportType());

        if (filters) {
            reportDTO.setFilters(filterSetToFilterDTOSet(report.getFilters()));
        }

        if (features != null) {
            List<VisibilityEnum> visibilityEnumList = AuthorizationCheckUtil.listAllowedVisibilityOptions(report.getDetails().getCreatedBy(), currentUser, features);

            if (!visibilityEnumList.isEmpty()) {
                reportDTO.setShareable(visibilityEnumList);
            }

            reportDTO.setDeletable(AuthorizationCheckUtil.isDeleteAllowed(reportDTO, currentUser, features));
            reportDTO.setEditable(AuthorizationCheckUtil.isEditAllowed(reportDTO, currentUser, features));
        }
        return reportDTO;
    }

    public Report reportDTOToReport(final ReportDTO dto) {
        if (dto == null) {
            return null;
        }
        Report report = factory.createReport();
        report.setDetails(new ReportDetails(
                dto.getDescription(), dto.getName(), dto.getWithMap(), dto.getScopeName(), dto.getCreatedBy())
        );
        report.setFilters(filterDTOSetToFilterSet(dto.getFilters(), report));
        report.setExecutionLogs(executionLogDTOToExecutionLogSet(dto.getExecutionLog(), report));
        report.setIsDeleted(dto.isDeleted());
        report.setDeletedOn(dto.getDeletedOn());
        report.setDeletedBy(dto.getDeletedBy());
        report.setVisibility(dto.getVisibility());
        report.setReportType(dto.getReportTypeEnum());
        return report;
    }

    private ExecutionLogDTO getExecutionLogDTO(Set<ExecutionLog> executionLogs) {
        if (isNotEmpty(executionLogs)) {
            return executionLogMapper.executionLogFilterToExecutionLogFilterDTO(executionLogs.iterator().next());
        }
        return null;
    }

    private Set<ExecutionLog> executionLogDTOToExecutionLogSet(ExecutionLogDTO executionLogDto, Report report) {
        if (executionLogDto != null) {
            ExecutionLog executionLog = new ExecutionLog(report, executionLogDto.getExecutedBy());
            return Sets.newHashSet(executionLog);
        }
        return Collections.emptySet();
    }

    public List<FilterDTO> filterSetToFilterDTOSet(final Set<Filter> filterSet) {
        if (filterSet == null) {
            return null;
        }
        List<FilterDTO> filterDTOSet = new ArrayList<>();
        for (Filter filter : filterSet) {
            FilterDTO filterDTO = filter.accept(new Filter.FilterToDTOVisitor());
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

}