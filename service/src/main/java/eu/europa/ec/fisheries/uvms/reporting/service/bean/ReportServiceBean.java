package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
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
    private SpatialService spatialModule;

    @IAuditInterceptor(auditActionType = AuditActionEnum.CREATE)
    @Transactional
    public ReportDTO create(ReportDTO report) throws ReportingServiceException {
        ReportDTO reportDTO = saveReport(report);

        saveMapConfiguration(reportDTO.getId(), null, report);

        return reportDTO;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private ReportDTO saveReport(ReportDTO report) {
        try {
            ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
            Report reportEntity = mapper.reportDTOToReport(report);
            reportEntity = repository.createEntity(reportEntity); // TODO @Greg mapping in repository
            ReportDTO reportDTO = mapper.reportToReportDTO(reportEntity);
            return reportDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error during the creation of the report");
        }
    }

    private boolean saveMapConfiguration(Long reportId, Long spatialConnectId, ReportDTO report) {
        boolean isSuccess = false;
        if (report.getWithMap()) {
            try {
                MapConfigurationDTO mapConfiguration = report.getMapConfiguration();
                isSuccess = spatialModule.saveOrUpdateMapConfiguration(reportId, spatialConnectId, mapConfiguration);
                if (!isSuccess) {
                    throw new RuntimeException("Error during saving or updating map configuration in spatial module");
                }
            } catch (ReportingServiceException e) {
                throw new RuntimeException("Error during saving or updatine map configuration in spatial module");
            }
        }
        return isSuccess;
    }

    public ReportDTO findById(long id, String username, String scopeName) throws ReportingServiceException {
        ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
        Report reportByReportId = repository.findReportByReportId(id, username, scopeName);
        return mapper.reportToReportDTO(reportByReportId);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @IAuditInterceptor(auditActionType = AuditActionEnum.MODIFY)
    public boolean update(final ReportDTO report) throws ReportingServiceException {
        validateReport(report);

        boolean update = repository.update(report);

        Long spatialConnectId = null; //TODO assign value
        saveMapConfiguration(report.getId(), spatialConnectId, report);
        spatialModule.saveOrUpdateMapConfiguration(report.getId(), spatialConnectId, report.getMapConfiguration());

        return update;
    }

    private void validateReport(ReportDTO report) {
        if (report == null) {
            throw new IllegalArgumentException("REPORT CAN NOT BE NULL");
        }
    }

    @IAuditInterceptor(auditActionType = AuditActionEnum.DELETE)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
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
