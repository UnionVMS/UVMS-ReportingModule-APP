package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.service.Merger;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * //TODO create test
 */
public class FilterMerger extends Merger<FilterDTO, Filter> {

    private FilterDAO filterDAO;

    public FilterMerger(final EntityManager em) {
        this.filterDAO = new FilterDAO(em);
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
        Long reportId = null;
        while (iterator.hasNext()){
            FilterDTO next = iterator.next();
            reportId = next.getReportId();
            if(reportId != null){
                break;
            }
        }
        return filterDAO.listByReportId(reportId);
    }

    @Override
    protected boolean merge(Filter incoming, Filter existing) throws ServiceException {

        boolean merge = !existing.equals(incoming);

        if (merge){
           existing.merge(incoming);
        }

        return merge;
    }

    @Override
    protected void insert(Filter item) throws ServiceException {
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
