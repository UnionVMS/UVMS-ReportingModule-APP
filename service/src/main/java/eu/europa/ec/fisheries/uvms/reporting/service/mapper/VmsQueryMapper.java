package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.*;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * //TODO create test
 */
public class VmsQueryMapper {

    private List<ListCriteria> movementListCriterias;
    private List<ListCriteria> connectIdMovements;
    private List<VesselListCriteriaPair> vesselListCriteriaPairs;
    private List<VesselGroup> vesselGroupList;

    @Builder(builderMethodName = "VmsQueryMapperBuilder")
    public VmsQueryMapper(Set<Filter> filters){
        movementListCriterias = new ArrayList<>();
        vesselListCriteriaPairs = new ArrayList<>();
        vesselGroupList = new ArrayList<>();
        connectIdMovements = new ArrayList<>();

        for (Object next : safe(filters)) {
            Filter filter = (Filter) next;
            switch (filter.getType()) {

                case VESSEL:
                    addToVesselCriteria(filter);
                    addConnectIdToMovementCriteria(filter);
                    break;
                case VGROUP:
                    addToVesselGroupCriteria(filter);
                    break;
                case DATETIME:
                    addToMovementCriteria(filter);
                    break;
                case VMSPOS:
                    break;
                default:
                    break;
            }
        }
    }

    public static Set safe( Set other ) {
        return other == null ? Collections.EMPTY_SET : other;
    }

    private void addConnectIdToMovementCriteria(Filter next) {
        VesselFilter filter = (VesselFilter) next;
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.CONNECT_ID);
        listCriteria.setValue(filter.getGuid());
        connectIdMovements.add(listCriteria);
    }

    private void addToVesselGroupCriteria(final Filter filter) {
        sanityCheck(filter);
        VesselGroup vesselGroup = new VesselGroup();
        VesselGroupFilter vesselGroupFilter = (VesselGroupFilter) filter;
        if (StringUtils.isNotBlank(vesselGroupFilter.getGroupId())) {
            vesselGroup.setId(new BigInteger(vesselGroupFilter.getGroupId()));
            vesselGroup.setDynamic(false);
            vesselGroupList.add(vesselGroup);
        }
    }

    private void addToMovementCriteria(final Filter filter) {
        sanityCheck(filter);
        DateTimeFilter dateTimeFilter = (DateTimeFilter) filter;
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.FROM_DATE);
        listCriteria.setValue(String.valueOf(dateTimeFilter.getStartDate()));
        movementListCriterias.add(listCriteria);
    }

    private void sanityCheck(Filter filter) {
        if (filter == null){
            throw new IllegalArgumentException("Filter can not be null.");
        }
    }

    public MovementQuery getMovementQuery() {
        MovementQuery movementQuery = new MovementQuery();
        movementQuery.getMovementSearchCriteria().addAll(movementListCriterias);
        movementQuery.getMovementSearchCriteria().addAll(connectIdMovements);
        ListPagination pagination = new ListPagination();
        pagination.setListSize(new BigInteger("1000"));
        pagination.setPage(new BigInteger("1000"));
        movementQuery.setPagination(pagination);
        return movementQuery;
    }


    private void addToVesselCriteria(final Filter filter){
        sanityCheck(filter);
        VesselFilter vesselFilter = (VesselFilter) filter;

        if(StringUtils.isNotBlank(vesselFilter.getGuid())){
            VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
            criteriaPair.setKey(ConfigSearchField.GUID);
            criteriaPair.setValue(vesselFilter.getGuid());
            vesselListCriteriaPairs.add(criteriaPair);
        }
    }

    public VesselListQuery getVesselListQuery() {

        VesselListQuery query = null;

        if (vesselListCriteriaPairs != null && vesselListCriteriaPairs.size()>0){
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
        return hasVessels() || hasVesselGroups();
    }

    public boolean hasVessels() {
        return vesselListCriteriaPairs != null && vesselListCriteriaPairs.size() > 0;
    }

    public boolean hasVesselGroups() {
        return vesselGroupList != null && vesselGroupList.size() > 0;
    }
}
