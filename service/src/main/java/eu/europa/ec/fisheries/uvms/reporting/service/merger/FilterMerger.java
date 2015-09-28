package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.DTOToFilterVisitor;

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
    protected Object getUniqKey(final Filter item) throws ReportingServiceException {
        return item.getId();
    }

    @Override
    protected Collection<Filter> convert(FilterDTO input) throws ReportingServiceException {
        Filter accept = input.accept(new DTOToFilterVisitor());
        Set<Filter> filterSet = new HashSet<>(1);
        filterSet.add(accept);
        return filterSet;
    }

    @Override
    protected Collection<Filter> loadCurrents(Collection<FilterDTO> input) throws ReportingServiceException {
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
    protected boolean merge(Filter incoming, Filter existing) throws ReportingServiceException {

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
            else if (existing instanceof PositionFilter){
                ((PositionFilter) existing).setEndDate(((PositionFilter) incoming).getEndDate());
                ((PositionFilter) existing).setStartDate(((PositionFilter) incoming).getStartDate());
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
    protected void insert(Filter item) throws ReportingServiceException {
        filterDAO.createEntity(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void update(Filter item) throws ReportingServiceException {
        filterDAO.updateEntity(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void delete(Filter item) throws ReportingServiceException {
        filterDAO.deleteEntity(Filter.class, item.getId());
    }
}
