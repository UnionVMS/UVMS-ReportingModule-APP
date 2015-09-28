package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.service.CrudService;

import java.util.List;

public interface ReportRepository extends CrudService {

    public Report saveOrUpdate(Report transientInstance);

    public Report saveOrUpdate(ReportDTO reportDTO);

    public Report findReportByReportId(Long reportId);

    public List<Report> listByUsernameAndScope(String username, long scopeId);

    public void persist(ExecutionLog transientInstance); // FIXME move to ExecutionLog Dao

    public void remove(Long reportId) throws ReportingServiceException;

}
