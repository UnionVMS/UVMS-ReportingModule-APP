package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Session Bean implementation class ReportBean
 * <p/>
 * TODO: add Authorization
 * TODO: add proper exception handling
 * TODO: add business validation
 * TODO: add data type validation
 */
@Stateless
@LocalBean
public class ReportServiceBean {

    @EJB
    private ReportRepository repository;

    @EJB
    private SpatialService spatialService;

    @IAuditInterceptor(auditActionType = AuditActionEnum.CREATE)
    @Transactional
    public ReportDTO create(ReportDTO report) throws ReportingServiceException {
        ReportDTO reportDTO = saveReport(report);
        saveMapConfiguration(reportDTO.getId(), report);
        return reportDTO;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private ReportDTO saveReport(ReportDTO report) {
        try {
            ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
            Report reportEntity = mapper.reportDTOToReport(report);
            reportEntity = repository.createEntity(reportEntity);
            ReportDTO reportDTO = mapper.reportToReportDTO(reportEntity);
            return reportDTO;
        }
        catch (Exception e) {
            throw new RuntimeException("Error during the creation of the report");
        }
    }

    private void saveMapConfiguration(Long reportId, ReportDTO report) {
        if (report.getMapConfiguration() != null) {
            try {
                spatialService.saveMapConfiguration(reportId, report.getMapConfiguration());
            } catch (ReportingServiceException e) {
                throw new RuntimeException("Error during saving map configuration in spatial module");
            }
        }
    }

    public ReportDTO findById(long id, String username, String scopeName) throws ReportingServiceException {
        ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
        return mapper.reportToReportDTO(repository.findReportByReportId(id, username, scopeName));
    }

    @IAuditInterceptor(auditActionType = AuditActionEnum.MODIFY)
    @Transactional
    public boolean update(ReportDTO report) throws ReportingServiceException {
        return repository.update(report);
    }

    @IAuditInterceptor(auditActionType = AuditActionEnum.DELETE)
    @Transactional
    public void delete(Long reportId, String username, String scopeName) throws ReportingServiceException {
        repository.remove(reportId, username, scopeName);
    }

    public Collection<ReportDTO> listByUsernameAndScope(final Set<String> features, final String username, final String scopeName) throws ReportingServiceException {
        ReportMapper mapper = ReportMapper.ReportMapperBuilder().features(features).currentUser(username).build();
        List<Report> reports = repository.listByUsernameAndScope(username, scopeName);
        List<ReportDTO> toReportDTOs = new ArrayList<>();
        for (Report report : reports) {
            toReportDTOs.add(mapper.reportToReportDTO(report));
        }
        return toReportDTOs;
    }

}
