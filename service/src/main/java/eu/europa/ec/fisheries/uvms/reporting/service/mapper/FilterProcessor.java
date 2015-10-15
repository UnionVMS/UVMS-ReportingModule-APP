package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
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
 * to search queries for vessels and movement queues
 */
public class FilterProcessor {

    private final List<ListCriteria> movementListCriteria = new ArrayList<>();
    private final List<ListCriteria> connectIdMovements = new ArrayList<>();
    private final List<VesselListCriteriaPair> vesselListCriteriaPairs = new ArrayList<>();
    private final List<VesselGroup> vesselGroupList = new ArrayList<>();

    public FilterProcessor init(Set<Filter> filters) throws ProcessorException {
        for (Object next : safe(filters)) {
            Filter filter = (Filter) next;
            switch (filter.getType()) {

                case vessels:
                    addToVesselCriteria(filter);
                    addConnectIdToMovementCriteria(filter);
                    break;
                case vgroup:
                    addToVesselGroupCriteria(filter);
                    break;
                case common:
                    addToMovementCriteria(filter);
                    break;
                case vmspos:
                    break;
                default:
                    break;
            }
        }
        return this;
    }

    private Set safe( Set other ) throws ProcessorException {
        if (other == null || other.size() == 0) {
            throw new ProcessorException("Unable to process empty filter list or filter list is null.");
        }
        return other;
    }

    private void addConnectIdToMovementCriteria(Filter next) {
        VesselFilter filter = (VesselFilter) next;
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.CONNECT_ID);
        listCriteria.setValue(filter.getGuid());
        connectIdMovements.add(listCriteria);
    }

    private void addToVesselGroupCriteria(final Filter filter) {
        VesselGroup vesselGroup = new VesselGroup();
        VesselGroupFilter vesselGroupFilter = (VesselGroupFilter) filter;
        if (StringUtils.isNotBlank(vesselGroupFilter.getGuid())) {
            vesselGroup.setId(new BigInteger(vesselGroupFilter.getGuid()));
            vesselGroup.setDynamic(false);
            vesselGroupList.add(vesselGroup);
        }
    }

    private void addToMovementCriteria(final Filter filter) {
        CommonFilter commonFilter = (CommonFilter) filter;

        PositionSelector positionSelector = commonFilter.getPositionSelector();

        switch(positionSelector.getSelector()){
            case ALL:
                movementListCriteria.addAll(processAll(commonFilter));
                break;
            case LAST:
                Position position = positionSelector.getPosition();
                switch(position){
                    case hours:
                        movementListCriteria.addAll(processLastHours(commonFilter));
                        break;
                    case positions:
                        movementListCriteria.addAll(processLastPositions(commonFilter));
                        break;
                    default:

                }

                break;
            default:

        }
    }

    private List<ListCriteria> processLastPositions(final CommonFilter dateTimeFilter) {
        Float positions = dateTimeFilter.getPositionSelector().getValue();
        throw new NotImplementedException("Not implemented in Movement API");
    }

    private List<ListCriteria> processLastHours(final CommonFilter dateTimeFilter) {
        Float hours = dateTimeFilter.getPositionSelector().getValue();
        DateTime currentDate = nowUTC();
        Date toDate = DateUtils.nowUTCMinusHours(currentDate, hours.intValue()).toDate();// FIXME hours!
        List<ListCriteria> listCriterias = new ArrayList<>();
        add(DateUtils.dateToString(toDate), listCriterias, SearchKey.FROM_DATE);
        add(DateUtils.dateToString(currentDate.toDate()), listCriterias, SearchKey.TO_DATE);
        return listCriterias;
    }

    private List<ListCriteria> processAll(final CommonFilter dateTimeFilter) {
        List<ListCriteria> listCriterias = new ArrayList<>();
        add(DateUtils.dateToString(dateTimeFilter.getStartDate()), listCriterias, SearchKey.FROM_DATE);
        add(DateUtils.dateToString(dateTimeFilter.getEndDate()), listCriterias, SearchKey.TO_DATE);
        return listCriterias;
    }

    private void add(final Object object, final List<ListCriteria> criteriaList, final SearchKey key) {
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(key);
        criteria.setValue(String.valueOf(object));
        criteriaList.add(criteria);
    }
    // UT
    protected DateTime nowUTC() {
        return DateUtils.nowUTC();
    }

    public MovementQuery toMovementQuery() {
        MovementQuery movementQuery = new MovementQuery();
        movementQuery.getMovementSearchCriteria().addAll(movementListCriteria);
        movementQuery.getMovementSearchCriteria().addAll(connectIdMovements);
        ListPagination pagination = new ListPagination();
        pagination.setListSize(new BigInteger("1000"));
        pagination.setPage(new BigInteger("1000"));
        movementQuery.setPagination(pagination);
        return movementQuery;
    }

    private void addToVesselCriteria(final Filter filter){
        VesselFilter vesselFilter = (VesselFilter) filter;
        if(StringUtils.isNotBlank(vesselFilter.getGuid())){
            VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
            criteriaPair.setKey(ConfigSearchField.GUID);
            criteriaPair.setValue(vesselFilter.getGuid());
            vesselListCriteriaPairs.add(criteriaPair);
        }
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
