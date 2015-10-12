package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.movement.model.util.DateUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterExpression {

    private static final int LIST_SIZE = 1000;

    public FilterExpression() {
        super();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_TIME_UI_FORMAT)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_TIME_UI_FORMAT)
    private Date endDate;

    private PositionSelector positionSelector;

    private List<eu.europa.ec.fisheries.uvms.reporting.model.Vessel> vessels;

    private Vms vms;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<eu.europa.ec.fisheries.uvms.reporting.model.Vessel> getVessels() {
        return vessels;
    }

    public void setVessels(List<eu.europa.ec.fisheries.uvms.reporting.model.Vessel> vessels) {
        this.vessels = vessels;
    }

    public Vms getVms() {
        return vms;
    }

    public void setVms(Vms vms) {
        this.vms = vms;
    }

    public PositionSelector getPositionSelector() {
        return positionSelector;
    }

    public void setPositionSelector(PositionSelector positionSelector) {
        this.positionSelector = positionSelector;
    }

    public static class Vms {

        private Positions positions;
        private Segments segments;
        private Tracks tracks;

        public Positions getPositions() {
            return positions;
        }

        public void setPositions(Positions positions) {
            this.positions = positions;
        }

        public Segments getSegments() {
            return segments;
        }

        public void setSegments(Segments segments) {
            this.segments = segments;
        }

        public Tracks getTracks() {
            return tracks;
        }

        public void setTracks(Tracks tracks) {
            this.tracks = tracks;
        }
    }

    public static class Tracks {

        private boolean active;

        public boolean getActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class Segments {

        private boolean active;

        public boolean getActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class Positions {

        private boolean active;

        public boolean getActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static enum PositionSelector {

        ALL("all"), LAST("last");

        private String name;

        private PositionSelector(final String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

    }

    public MovementQuery createMovementQuery(final FilterExpression filter) {
        MovementQuery movementQuery = new MovementQuery();
        ListPagination pagination = new ListPagination();
        pagination.setPage(BigInteger.valueOf(1));
        pagination.setListSize(BigInteger.valueOf(LIST_SIZE));
        movementQuery.setPagination(pagination);
        movementQuery.getMovementSearchCriteria().addAll(createMovementListCriteria(filter));
        return movementQuery;
    }

    private List<ListCriteria> createMovementListCriteria(final Date startDate,
                                                          final Date endDate, final List<ListCriteria> criteria){
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.FROM_DATE);
        listCriteria.setValue(DateUtil.parseUTCDateToString(startDate));
        criteria.add(listCriteria);
        listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.TO_DATE);
        listCriteria.setValue(DateUtil.parseUTCDateToString(endDate));
        criteria.add(listCriteria);
        return criteria;
    }

    private List<ListCriteria> createMovementListCriteria(final FilterExpression filter){

        FilterExpression.PositionSelector positionSelector = filter.getPositionSelector();
        List<ListCriteria> criterias = new ArrayList<>();

        switch (positionSelector) {
            case ALL:
                Date startDate = filter.getStartDate();
                Date endDate = filter.getEndDate();
                createMovementListCriteria(startDate, endDate, criterias);
                break;
            default:
        }

        return criterias;
    }
}
