package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.Builder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * This class is responsible reading the report filters and transform them
 * to search queries for vessel and movement queues
 */
public class FilterProcessor {

    private final List<ListCriteria> movementListCriteria = new ArrayList<>();
    private final List<RangeCriteria> rangeCriteria = new ArrayList<>();
    private final List<VesselListCriteriaPair> vesselListCriteriaPairs = new ArrayList<>();
    private final List<VesselGroup> vesselGroupList = new ArrayList<>();

    public FilterProcessor(Set<Filter> filters) throws ProcessorException {
        validate(filters);

        for (Filter filter : filters) {
            addCriteria(filter);
        }
    }

    private void addCriteria(Filter filter) {
        vesselListCriteriaPairs.addAll(filter.vesselCriteria());
        vesselGroupList.addAll(filter.vesselGroupCriteria());
        rangeCriteria.addAll(filter.movementRangeCriteria());
        movementListCriteria.addAll(filter.movementCriteria());
    }

    private void validate(Set<Filter> filters) throws ProcessorException {
        if (isEmpty(filters)) {
            throw new ProcessorException("Unable to process empty filter list or filter list is null.");
        }
    }

    public MovementQuery toMovementQuery() {
        MovementQuery movementQuery = new MovementQuery();
        movementQuery.getMovementSearchCriteria().addAll(movementListCriteria);
        movementQuery.getMovementRangeSearchCriteria().addAll(rangeCriteria);
        ListPagination pagination = new ListPagination();
        pagination.setListSize(new BigInteger("10"));
        pagination.setPage(new BigInteger("1"));
        movementQuery.setPagination(pagination);
        return movementQuery;
    }

    public VesselListQuery toVesselListQuery() {
        VesselListQuery query = new VesselListQuery();

        if (isNotEmpty(vesselListCriteriaPairs)) {
            query.setVesselSearchCriteria(createListCriteria());
            query.setPagination(createPagination());
        }

        return query;
    }

    private VesselListPagination createPagination() {
        VesselListPagination pagination = new VesselListPagination();
        pagination.setPage(1);
        pagination.setListSize(1000);
        return pagination;
    }

    private VesselListCriteria createListCriteria() {
        VesselListCriteria vesselListCriteria = new VesselListCriteria();
        vesselListCriteria.setIsDynamic(false);
        vesselListCriteria.getCriterias().addAll(vesselListCriteriaPairs);
        return vesselListCriteria;
    }

    public List<VesselGroup> getVesselGroupList() {
        return vesselGroupList;
    }

    public boolean hasVesselsOrVesselGroups() {
        return hasVessels() | hasVesselGroups();
    }

    public boolean hasVessels() {
        return vesselListCriteriaPairs.size() > 0;
    }

    public boolean hasVesselGroups() {
        return vesselGroupList.size() > 0;
    }

    public List<ListCriteria> getMovementListCriteria() {
        return movementListCriteria;
    }

    public List<VesselListCriteriaPair> getVesselListCriteriaPairs() {
        return vesselListCriteriaPairs;
    }

    public List<RangeCriteria> getRangeCriteria() {
        return rangeCriteria;
    }
}
