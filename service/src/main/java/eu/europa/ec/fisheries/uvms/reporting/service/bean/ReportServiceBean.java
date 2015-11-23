package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingJMSConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;

/**
 * Session Bean implementation class ReportBean
 * 
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

    @IAuditInterceptor(auditActionType=AuditActionEnum.CREATE)
    public ReportDTO create(ReportDTO report) throws ReportingServiceException {
        ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
    	Report reportEntity = mapper.reportDTOToReport(report);
		reportEntity = repository.createEntity(reportEntity);
        spatialService.saveMapConfiguration(report.getMapConfiguration());
    	return mapper.reportToReportDTO(reportEntity);
    }
	
	public ReportDTO findById(long id, String username, String scopeName) throws ReportingServiceException {
        ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
		return mapper.reportToReportDTO(repository.findReportByReportId(id, username, scopeName));
	}

	@IAuditInterceptor(auditActionType=AuditActionEnum.MODIFY)
    public boolean update(ReportDTO report) throws ReportingServiceException {
        return repository.update(report);
    }
	
	@IAuditInterceptor(auditActionType=AuditActionEnum.DELETE)
    @Transactional
    public void delete(Long reportId, String username, String scopeName) throws ReportingServiceException {
		repository.remove(reportId, username, scopeName);
	}

	public Collection<ReportDTO> listByUsernameAndScope(final Set<String> features, final String username, final String scopeName) throws ReportingServiceException {
        ReportMapper mapper = ReportMapper.ReportMapperBuilder().features(features).currentUser(username).build();
		List<Report> reports = repository.listByUsernameAndScope(username, scopeName);
        List<ReportDTO> toReportDTOs = new ArrayList<>();
        for (Report report : reports){
            toReportDTOs.add(mapper.reportToReportDTO(report));
        }
        return toReportDTOs;
	}

}
