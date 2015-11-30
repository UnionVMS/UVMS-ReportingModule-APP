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

        saveOrUpdateMapConfiguration(reportDTO.getId(), report.getWithMap(), report.getMapConfiguration());

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

    private boolean saveOrUpdateMapConfiguration(long reportId, Boolean withMap, MapConfigurationDTO mapConfiguration) {
        if (withMap) {
            try {
                boolean isSuccess = spatialModule.saveOrUpdateMapConfiguration(reportId, mapConfiguration);
                if (!isSuccess) {
                    throw new RuntimeException("Error during saving or updating map configuration in spatial module");
                }
            } catch (ReportingServiceException e) {
                throw new RuntimeException("Error during saving or updatine map configuration in spatial module");
            }
        } else {
            // TODO Remove Map Configuration from Spatial when old value of withMap was set to true
        }
        return true;
    }

    @Transactional
    public ReportDTO findById(long id, String username, String scopeName) {
        ReportDTO reportDTO = readReport(id, username, scopeName);

        populateMapConfiguration(id, reportDTO);

        return reportDTO;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private ReportDTO readReport(long id, String username, String scopeName) {
        try {
            ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
            Report reportByReportId = repository.findReportByReportId(id, username, scopeName);
            return mapper.reportToReportDTO(reportByReportId);
        } catch (Exception e) {
            throw new RuntimeException("Error during reading the report");
        }
    }

    private void populateMapConfiguration(long id, ReportDTO reportDTO) {
        try {
            MapConfigurationDTO mapConfiguratioDTO = spatialModule.getMapConfiguration(id);
            reportDTO.setMapConfiguration(mapConfiguratioDTO);
        } catch (ReportingServiceException e) {
            throw new RuntimeException("Error during reading map configuration in spatial module");
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @IAuditInterceptor(auditActionType = AuditActionEnum.MODIFY)
    public boolean update(final ReportDTO report) throws ReportingServiceException {
        validateReport(report);

        boolean update = repository.update(report);

        saveOrUpdateMapConfiguration(report.getId(), report.getWithMap(), report.getMapConfiguration());

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
