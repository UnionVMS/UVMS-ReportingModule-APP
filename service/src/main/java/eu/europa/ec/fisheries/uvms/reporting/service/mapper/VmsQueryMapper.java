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

import java.math.BigInteger;
import java.util.ArrayList;
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

    @Builder(builderMethodName = "buildVmsQuery")
    public VmsQueryMapper(Set<Filter> filters){
        movementListCriterias = new ArrayList<>();
        vesselListCriteriaPairs = new ArrayList<>();
        vesselGroupList = new ArrayList<>();
        connectIdMovements = new ArrayList<>();

        for (Filter next : filters) {

            switch (next.getType()) {

                case VESSEL:
                    addToVesselCriteria(next);
                    addConnectIdToMovementCriteria(next);
                    break;
                case VGROUP:
                    addToVesselGroupCriteria(next);
                    break;
                case DATETIME:
                    addToMovementCriteria(next);
                    break;
                case VMSPOS:
                    break;
                default:
                    break;
            }
        }
    }

    private void addConnectIdToMovementCriteria(Filter next) {
        VesselFilter filter = (VesselFilter) next;
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.CONNECT_ID);
        listCriteria.setValue(filter.getGuid());
        connectIdMovements.add(listCriteria);
    }

    private void addToVesselGroupCriteria(final Filter next) {
        VesselGroup vesselGroup = new VesselGroup();
        VesselGroupFilter filter = (VesselGroupFilter) next;
        vesselGroup.setId(new BigInteger(filter.getGroupId()));
        vesselGroup.setDynamic(false);
        vesselGroupList.add(vesselGroup);
    }

    private void addToMovementCriteria(Filter next) {
        DateTimeFilter filter = (DateTimeFilter) next;
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.FROM_DATE);
        listCriteria.setValue(filter.getStartDate().toString());

        movementListCriterias.add(listCriteria);

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


    private void addToVesselCriteria(final Filter next){
        VesselFilter filter = (VesselFilter) next;
        VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(filter.getGuid());
        vesselListCriteriaPairs.add(criteriaPair);
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

    public List<VesselListCriteriaPair> getVesselListCriteriaPairs() {
        return vesselListCriteriaPairs;
    }

    public boolean hasVesselsOrVesselGroups() {
        return vesselListCriteriaPairs != null || vesselGroupList != null;
    }
}
