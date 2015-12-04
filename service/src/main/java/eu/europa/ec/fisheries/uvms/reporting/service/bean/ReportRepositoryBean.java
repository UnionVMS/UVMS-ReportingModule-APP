package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ExecutionLogDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.FilterMerger;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.ReportMerger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

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
    public void postConstruct(){
        reportDAO = new ReportDAO(em);
        filterDAO = new FilterDAO(em);
        executionLogDAO = new ExecutionLogDAO(em);
        filterMerger = new FilterMerger(em);
        reportMerger = new ReportMerger(em);
    }

    @Override
    @Transactional
    public boolean update(final ReportDTO reportDTO) throws ReportingServiceException {

        boolean merged;

        try {

            merged = reportMerger.merge(Arrays.asList(reportDTO));

            List<FilterDTO> filters = reportDTO.getFilters();

            if (CollectionUtils.isNotEmpty(filters)) {

                merged = filterMerger.merge(filters);

          }

        } catch (ServiceException e) {

            String message = "UPDATE FAILED";

            log.error(message, e);

            throw new ReportingServiceException(message, e);

        }

        return true;

    }

    @Override
    public Report findReportByReportId(final Long id, String username, String scopeName) throws ReportingServiceException {
        return reportDAO.findReportByReportId(id, username, scopeName);
    }

    @Override
    public List<Report> listByUsernameAndScope(final String username, final String scopeName) throws ReportingServiceException {
        return reportDAO.listByUsernameAndScope(username, scopeName);
    }

    @Override
    @Transactional
    public void remove(final Long reportId, String username, String scopeName) throws ReportingServiceException {
        reportDAO.softDelete(reportId, username, scopeName);
    }

    @Override
    @Transactional
    public Report createEntity(Report report) throws ReportingServiceException {
        try {
            return reportDAO.createEntity(report);
        } catch (ServiceException e) {
            log.error("createEntity failed", e);
            throw new ReportingServiceException("createEntity failed", e);
        }
    }
}
