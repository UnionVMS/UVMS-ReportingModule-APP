package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.service.Merger;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * //TODO create test
 */
public class FilterMerger extends Merger<FilterDTO, Filter> {

    private FilterDAO filterDAO;
    private ReportDAO reportDAO;

    public FilterMerger(final EntityManager em) {
        this.filterDAO = new FilterDAO(em);
        this.reportDAO = new ReportDAO(em);
    }

    @Override
    protected Object getUniqKey(final Filter item) throws ServiceException {
        return item.getId();
    }

    @Override
    protected Collection<Filter> convert(FilterDTO input) throws ServiceException {
        Filter filter = input.convertToFilter();
        Set<Filter> filterSet = new HashSet<>(1);
        filterSet.add(filter);
        return filterSet;
    }

    @Override
    protected Collection<Filter> loadCurrents(Collection<FilterDTO> input) throws ServiceException {
        Iterator<FilterDTO> iterator = input.iterator();
        List<Long> filterIds = new ArrayList<>();
        while (iterator.hasNext()){
            FilterDTO next = iterator.next();
            Long id = next.getId();
            if (id != null){
                filterIds.add(next.getId());
            }
        }
        return filterDAO.listById(filterIds);
    }

    @Override
    protected boolean merge(Filter incoming, Filter existing) throws ServiceException {

        boolean merge = !existing.equals(incoming);

        if (merge){
           incoming.setReport(existing.getReport());
           existing.merge(incoming);
        }

        return merge;
    }

    @Override
    protected void insert(Filter item) throws ServiceException {
        Report entityById = reportDAO.findEntityById(Report.class, item.getReportId());
        item.setReport(entityById);
        filterDAO.createEntity(item);
    }

    @Override
    protected void update(Filter item) throws ServiceException {
        filterDAO.updateEntity(item);
    }

    @Override
    protected void delete(Filter item) throws ServiceException {
        filterDAO.deleteEntity(item, item.getId());
    }
}
