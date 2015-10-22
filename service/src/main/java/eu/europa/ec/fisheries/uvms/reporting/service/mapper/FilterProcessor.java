package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.*;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.Builder;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
            switch (filter.getType()) {

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
                    break;
                case vmstrack:
                    movementListCriteria.addAll(filter.movementListCriteria());
                    rangeCriteria.addAll(filter.movementRangeCriteria());
                    break;
                default:
                    break;
            }
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
