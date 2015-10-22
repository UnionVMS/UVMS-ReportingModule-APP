package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

/**
 * This class is responsible reading the report filters and transform them
 * to search queries for vessel and movement queues
 */
public class FilterProcessor {

    private final List<ListCriteria> movementListCriteria = new ArrayList<>();
    private final List<RangeCriteria> rangeCriteria = new ArrayList<>();

    private final List<ListCriteria> connectIdMovements = new ArrayList<>();
    private final List<VesselListCriteriaPair> vesselListCriteriaPairs = new ArrayList<>();
    private final List<VesselGroup> vesselGroupList = new ArrayList<>();

    @Builder(builderMethodName = "FilterProcessorBuilder")
    public FilterProcessor(Set<Filter> filters) throws ProcessorException {
        for (Object next : safe(filters)) {
            Filter filter = (Filter) next;
            
            vesselListCriteriaPairs.addAll(filter.vesselCriteria());
            connectIdMovements.addAll(filter.movementCriteria());
            vesselGroupList.addAll(filter.vesselGroupCriteria());
            rangeCriteria.addAll(filter.movementRangeCriteria());
        	movementListCriteria.addAll(filter.movementCriteria());
        	
/*            switch (filter.getType()) {
                case vessel:
                    vesselListCriteriaPairs.addAll(filter.vesselCriteria());
                    connectIdMovements.addAll(filter.movementCriteria());
                    break;
                case vgroup:
                    vesselGroupList.addAll(filter.vesselGroupCriteria());
                    break;
                case common:
                    rangeCriteria.addAll(filter.movementRangeCriteria());
                    break;
                case vmspos:
                	rangeCriteria.addAll(filter.movementRangeCriteria());
                	movementListCriteria.addAll(filter.movementCriteria());
                    break;
                case vmsseg:
                	rangeCriteria.addAll(filter.movementRangeCriteria());
                	movementListCriteria.addAll(filter.movementCriteria());
                case vmstrack:
                    movementListCriteria.addAll(filter.movementCriteria());
                    rangeCriteria.addAll(filter.movementRangeCriteria());
                    break;
                default:
                    break;
            }*/
        }
    }

    private Set safe( Set other ) throws ProcessorException {
        if (other == null || other.size() == 0) {
            throw new ProcessorException("Unable to process empty filter list or filter list is null.");
        }
        return other;
    }

    public MovementQuery toMovementQuery() {
        MovementQuery movementQuery = new MovementQuery();
        movementQuery.getMovementSearchCriteria().addAll(movementListCriteria);
        movementQuery.getMovementSearchCriteria().addAll(connectIdMovements);
        movementQuery.getMovementRangeSearchCriteria().addAll(rangeCriteria);
        ListPagination pagination = new ListPagination();
        pagination.setListSize(new BigInteger("10"));
        pagination.setPage(new BigInteger("1"));
        movementQuery.setPagination(pagination);
        return movementQuery;
    }

    public VesselListQuery toVesselListQuery() {

        VesselListQuery query = null;

        if (vesselListCriteriaPairs.size() > 0){
            query = new VesselListQuery();
            VesselListCriteria vesselListCriteria = new VesselListCriteria();
            vesselListCriteria.setIsDynamic(false);
            vesselListCriteria.getCriterias().addAll(vesselListCriteriaPairs);
            query.setVesselSearchCriteria(vesselListCriteria);
            VesselListPagination pagination = new VesselListPagination();
            pagination.setPage(BigInteger.valueOf(1));
            pagination.setListSize(new BigInteger("1000"));
            query.setPagination(pagination);
        }

        return query;
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

    public List<ListCriteria> getConnectIdMovements() {
        return connectIdMovements;
    }

    public List<VesselListCriteriaPair> getVesselListCriteriaPairs() {
        return vesselListCriteriaPairs;
    }
}
