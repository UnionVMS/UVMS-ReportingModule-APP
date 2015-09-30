package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ExecutionLogDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.FilterMerger;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.ReportMerger;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Stateless
@Local(ReportRepository.class)
@Slf4j
public class ReportRepositoryBean implements ReportRepository {

    private ReportDAO reportDAO;
    private FilterDAO filterDAO;
    private ExecutionLogDAO executionLogDAO;

    private FilterMerger filterMerger;

    private ReportMerger reportMerger;

    @PersistenceContext(unitName = "reportingPU")
    private EntityManager em;

    @PostConstruct
    public void postContruct(){
        reportDAO = new ReportDAO(em);
        filterDAO = new FilterDAO(em);
        executionLogDAO = new ExecutionLogDAO(em);
        filterMerger = new FilterMerger(em);
        reportMerger = new ReportMerger(em);
    }

    @Override
    @Transactional
    public Report saveOrUpdate(final Report report) throws ServiceException {
        return reportDAO.saveOrUpdate(report);
    }

    @Override
    @Transactional
    public boolean update(final ReportDTO reportDTO) throws ServiceException {

        boolean merge;

        reportMerger.merge(Arrays.asList(reportDTO));
        filterMerger.merge(reportDTO.getFilters());
        merge = true;

        return merge;
    }


    @Override
    @Transactional
    public Report findReportByReportId(final Long id) throws ServiceException {
        return reportDAO.findReportByReportId(id);
    }

    @Override
    public List<Report> listByUsernameAndScope(final String username, final long scopeId) throws ServiceException {
        return reportDAO.listByUsernameAndScope(username, scopeId);
    }

    @Override
    @Transactional
    public void remove(final Long reportId) throws ServiceException {
        reportDAO.remove(reportId);
    }

    @Override
    public void deleteEntity(Report report, Long id) throws ServiceException {
        reportDAO.deleteEntity(report, id);
    }

    @Override
    @Transactional
    public Report createEntity(Report report) throws ServiceException {
        return reportDAO.createEntity(report);
    }
}
