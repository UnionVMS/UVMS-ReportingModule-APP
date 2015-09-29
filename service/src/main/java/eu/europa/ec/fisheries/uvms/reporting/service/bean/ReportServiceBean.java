package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.Feature;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.FilterMerger;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;

import static eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper.*;

/**
 * Session Bean implementation class ReportBean
 * 
 * TODO: add Authorization
 * TODO: add proper exception handling
 * TODO: add XSD definition which can be used as data validation 
 * TODO: add business validation
 * TODO: add data type validation
 */
@Stateless
@LocalBean
public class ReportServiceBean {

    @EJB
    private ReportRepository repository;

    private FilterMerger merger;

    @IAuditInterceptor(auditActionType=AuditActionEnum.CREATE)
    public ReportDTO create(ReportDTO report) throws ServiceException {
        ReportMapper mapper = reportMapperBuilder().filters(true).build();
    	Report reportEntity = mapper.reportDtoToReport(report);
		reportEntity = repository.createEntity(reportEntity);
    	return mapper.reportToReportDto(reportEntity);
    }
	
	public ReportDTO findById(long id) throws ServiceException {
        ReportMapper mapper = reportMapperBuilder().filters(true).build();
		return mapper.reportToReportDto(repository.findReportByReportId(id));
	}
	
	@IAuditInterceptor(auditActionType=AuditActionEnum.MODIFY)
    public boolean update(ReportDTO report) throws ServiceException {
        return repository.update(report);
    }
	
	@IAuditInterceptor(auditActionType=AuditActionEnum.DELETE)
    @Transactional
    public void delete(Long reportId) throws ReportingServiceException, ServiceException {
		repository.remove(reportId);
	}
	
	public Collection runReport(Long reportId) {
		//TODO don't forget to add a new execution log entry !!!!
		return null;
	}

	public Collection<ReportDTO> listByUsernameAndScope(final Set<Feature> features, final String username, final long scopeID) throws ServiceException {
        ReportMapper mapper = reportMapperBuilder().features(features).currentUser(username).build();
		List<Report> reports = repository.listByUsernameAndScope(username, scopeID);
        List<ReportDTO> toReportDTOs = new ArrayList<>();
        for (Report report : reports){
            toReportDTOs.add(mapper.reportToReportDto(report));
        }
        return toReportDTOs;
	}

	public void executeReport(String username, long reportId) throws ServiceException {
		Report entity = repository.findReportByReportId(reportId);
        Date date = new Date();
		ExecutionLog logEntity = new ExecutionLog();
		logEntity.setExecutedBy(username);
		logEntity.setExecutedOn(date);
		logEntity.setReport(entity);

        repository.persist(logEntity);
	}


}
