package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.reporsitory.ExecutionLogDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.reporsitory.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.reporsitory.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.FilterMerger;
import eu.europa.ec.fisheries.uvms.service.AbstractCrudService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(ReportRepository.class)
@Slf4j
public class ReportRepositoryBean extends AbstractCrudService implements ReportRepository {

    private ReportDAO reportDAO;
    private FilterDAO filterDAO;

    private ExecutionLogDAO executionLogDAO;

    private FilterMerger filterMerger;

    @PersistenceContext(unitName = "reportingPU")
    private EntityManager em;

    @Resource
    private SessionContext context;

    @PostConstruct
    public void postContruct(){
        reportDAO = new ReportDAO(em);
        filterDAO = new FilterDAO(em);
        filterMerger = new FilterMerger(filterDAO);
        // FIXME add executionLogDao
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    @Transactional
    public Report saveOrUpdate(final Report report) {
        return reportDAO.saveOrUpdate(report);
    }

    @Override
    @Transactional
    public Report saveOrUpdate(final ReportDTO reportDTO) {
        try {
            boolean merge = filterMerger.merge(reportDTO.getFilters());
        } catch (ReportingServiceException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    @Transactional
    public Report findReportByReportId(final Long id) {
        return reportDAO.findReportByReportId(id);
    }

    @Override
    public List<Report> listByUsernameAndScope(final String username, final long scopeId) {
        return reportDAO.listByUsernameAndScope(username, scopeId);
    }

    @Override
    @Transactional
    public void persist(final ExecutionLog transientInstance) {
        reportDAO.persist(transientInstance);
    }

    @Override
    @Transactional
    public void remove(final Long reportId) throws ReportingServiceException {
        reportDAO.remove(reportId);
    }
}
