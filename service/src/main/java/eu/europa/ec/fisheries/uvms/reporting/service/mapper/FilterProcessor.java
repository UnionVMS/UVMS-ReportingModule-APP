package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.*;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * This class is responsible reading the report filters and transform them
 * to search queries for vessel and movement queues
 */
public class FilterProcessor {

    private final Set<ListCriteria> movementListCriteria = new HashSet<>();
    private final Set<RangeCriteria> rangeCriteria = new HashSet<>();
    private final Set<VesselListCriteriaPair> vesselListCriteriaPairs = new HashSet<>();
    private final Set<VesselGroup> vesselGroupList = new HashSet<>();
    private final Set<AreaIdentifierType> areaIdentifierList = new HashSet<AreaIdentifierType>();

    public FilterProcessor(Set<Filter> filters) throws ProcessorException {
        validate(filters);

        for (Filter filter : filters) {
            addCriteria(filter);
            addAreaIdentifier(filter);
        }
    }
    
    public void addAreaCriteria(String areaWkt) {
    	if (areaWkt != null) {
    		ListCriteria areaCriteria = new ListCriteria();
        	areaCriteria.setKey(SearchKey.AREA);
        	areaCriteria.setValue(areaWkt);
        	movementListCriteria.add(areaCriteria);
    	}    	
    }

    private void addCriteria(Filter filter) {
        vesselListCriteriaPairs.addAll(filter.vesselCriteria());
        vesselGroupList.addAll(filter.vesselGroupCriteria());
        rangeCriteria.addAll(filter.movementRangeCriteria());
        movementListCriteria.addAll(filter.movementListCriteria());
    }
    
    private void addAreaIdentifier(Filter filter) {
    	if (filter instanceof AreaFilter) {
    		areaIdentifierList.add(filter.getAreaIdentifierType());
    	}
    }

    private void validate(Set<Filter> filters) throws ProcessorException {
        if (isEmpty(filters)) {
            throw new ProcessorException("");
        }
    }

    public MovementQuery toMovementQuery() {
        MovementQuery movementQuery = new MovementQuery();
        movementQuery.getMovementSearchCriteria().addAll(movementListCriteria);
        movementQuery.getMovementRangeSearchCriteria().addAll(rangeCriteria);
        movementQuery.setExcludeFirstAndLastSegment(true);
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

    public Set<VesselGroup> getVesselGroupList() {
        return vesselGroupList;
    }

    public boolean hasVesselsOrVesselGroups() {
        return hasVessels() | hasVesselGroups();
    }

    public boolean hasVessels() {
        return !vesselListCriteriaPairs.isEmpty();
    }

    public boolean hasVesselGroups() {
        return !vesselGroupList.isEmpty();
    }

    public Set<ListCriteria> getMovementListCriteria() {
        return movementListCriteria;
    }

    public Set<VesselListCriteriaPair> getVesselListCriteriaPairs() {
        return vesselListCriteriaPairs;
    }

    public Set<RangeCriteria> getRangeCriteria() {
        return rangeCriteria;
    }
    
    public Set<AreaIdentifierType> getAreaIdentifierList() {
    	return areaIdentifierList;
    }
}
