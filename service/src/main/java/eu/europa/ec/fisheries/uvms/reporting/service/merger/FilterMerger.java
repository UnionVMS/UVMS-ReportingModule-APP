package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.DTOToFilterVisitor;
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
        Filter accept = input.accept(new DTOToFilterVisitor());
        Set<Filter> filterSet = new HashSet<>(1);
        filterSet.add(accept);
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
            if(existing instanceof VesselFilter){
                ((VesselFilter) existing).setGuid(((VesselFilter) incoming).getGuid());
                ((VesselFilter)existing).setName(((VesselFilter) incoming).getName());
            }
            else if (existing instanceof VesselGroupFilter){
                ((VesselGroupFilter) existing).setGuid(((VesselGroupFilter) incoming).getGuid());
                ((VesselGroupFilter)existing).setUserName(((VesselGroupFilter) incoming).getUserName());
                ((VesselGroupFilter)existing).setGroupId(((VesselGroupFilter) incoming).getGroupId());
            }
            else if (existing instanceof DateTimeFilter){
                ((DateTimeFilter) existing).setEndDate(((DateTimeFilter) incoming).getEndDate());
                ((DateTimeFilter) existing).setStartDate(((DateTimeFilter) incoming).getStartDate());
            }
            else if (existing instanceof VmsPositionFilter){
                ((VmsPositionFilter) existing).setMaximumSpeed(((VmsPositionFilter) incoming).getMaximumSpeed());
                ((VmsPositionFilter) existing).setMinimumSpeed(((VmsPositionFilter) incoming).getMinimumSpeed());
            }
        }

        return merge;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void insert(Filter item) throws ServiceException {
        filterDAO.createEntity(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void update(Filter item) throws ServiceException {
        filterDAO.updateEntity(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void delete(Filter item) throws ServiceException {
        filterDAO.deleteEntity(item, item.getId());
    }
}
