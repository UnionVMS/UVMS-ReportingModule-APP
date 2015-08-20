package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper;

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
public class ReportBean {

	@EJB
	private ReportDAO reportDAO;
	
    @Inject
    private ReportMapper mapper;
	
    /**
     * Default constructor. 
     */
    public ReportBean() {
        // TODO Auto-generated constructor stub
    }
    
    public Report create(Report report) {
    	ReportEntity reportEntity = mapper.reportDtoToReport(report);
		reportEntity = reportDAO.persist(reportEntity);
    	return mapper.reportToReportDto(reportEntity);
    }
	
	public Report findById(long id) {
		return mapper.reportToReportDto(reportDAO.findById(id));
	}
	
	public void update(Report report) {
		ReportEntity reportEntity = mapper.reportDtoToReport(report);
		reportDAO.persist(reportEntity);
	}
	
	public void delete(Long reportId) {
		reportDAO.remove(reportId);
	}
	
	public Collection runReport(Long reportId) {
		//TODO don't forget to add a new execution log entry !!!!
		return null;
	}
	
	
	public Collection<Report> findReports(String username, long scopeID) {
		Collection<ReportEntity> reports = reportDAO.findByUsernameAndScope(username, scopeID);
		
		Collection<Report> reportDTOs = mapper.reportsToReportDtos(reports);
		return reportDTOs;
	}

}
