package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

import java.util.List;

public interface ReportRepository {

    boolean update(ReportDTO reportDTO) throws ReportingServiceException;

    Report findReportByReportId(Long reportId, String username, String scopeName) throws ReportingServiceException;

    List<Report> listByUsernameAndScope(String username, String scopeName) throws ReportingServiceException;

    void remove(Long reportId, String username, String scopeName) throws ReportingServiceException;

    Report createEntity(Report reportEntity) throws ReportingServiceException;
}
