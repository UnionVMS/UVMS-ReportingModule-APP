package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

import java.util.List;

public interface ReportRepository {

    public Report saveOrUpdate(Report transientInstance) throws ServiceException;

    public boolean update(ReportDTO reportDTO) throws ServiceException;

    public Report findReportByReportId(Long reportId) throws ServiceException;

    public List<Report> listByUsernameAndScope(String username, long scopeId) throws ServiceException;

    public void remove(Long reportId) throws ServiceException;

    void deleteEntity(Report report, Long id) throws ServiceException;

    Report createEntity(Report reportEntity) throws ServiceException;
}
