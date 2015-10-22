package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.DateTimeFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("DATETIME")
@EqualsAndHashCode(callSuper = true, of = {"startDate", "endDate"})
public class CommonFilter extends Filter {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    @Embedded
    private PositionSelector positionSelector;

    CommonFilter(){
        super(FilterType.common);
    }

    @Builder(builderMethodName = "CommonFilterBuilder")
    public CommonFilter(Long id, Date startDate, Date endDate, PositionSelector positionSelector) {
        super(FilterType.common);
        this.startDate = startDate;
        this.endDate = endDate;
        this.positionSelector = positionSelector;
        setId(id);
    }

    @Override
    public FilterDTO convertToDTO() {
        return DateTimeFilterMapper.INSTANCE.dateTimeFilterToDateTimeFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        CommonFilter incoming = (CommonFilter) filter;
        setEndDate(incoming.getEndDate());
        setStartDate(incoming.getStartDate());
    }

    @Override
    public RangeCriteria movementRangeCriteria(){
        PositionSelector positionSelector = getPositionSelector();
        RangeCriteria criteria = null;

        switch(positionSelector.getSelector()){
            case all:
                criteria = new RangeCriteria();
                criteria.setKey(RangeKeyType.DATE);
                criteria.setFrom(DateUtils.dateToString(getStartDate()));
                criteria.setTo(DateUtils.dateToString(getEndDate()));
                break;
            case last:
                Position position = positionSelector.getPosition();
                switch(position){
                    case hours:
                       // rangeCriteria.addAll(processLastHours(commonFilter));
                        break;
                    case positions:
                       // movementListCriteria.addAll(processLastPositions(commonFilter));
                        break;
                    default:

                }

                break;
            default:

        }
        return criteria;
    }

    private List<ListCriteria> processLastPositions(final CommonFilter dateTimeFilter) {
        //  Float positions = dateTimeFilter.getPositionSelector().getValue();
        throw new NotImplementedException("Not implemented in Movement API");
    }

    private List<RangeCriteria> processLastHours(final CommonFilter dateTimeFilter) {
        Float hours = dateTimeFilter.getPositionSelector().getValue();
        DateTime currentDate = nowUTC();
        Date toDate = DateUtils.nowUTCMinusSeconds(currentDate, hours).toDate();
        List<RangeCriteria> rangeCriterias = new ArrayList<>();
        addRangeCriteria(DateUtils.dateToString(toDate),
                DateUtils.dateToString(currentDate.toDate()), rangeCriterias, RangeKeyType.DATE);
        return rangeCriterias;
    }

    // UT
    protected DateTime nowUTC() {
        return DateUtils.nowUTC();
    }


    private void addRangeCriteria(final String from, final String to, final List<RangeCriteria> rangeCriteria, final RangeKeyType key) {
        RangeCriteria criteria = new RangeCriteria();
        criteria.setKey(key);
        criteria.setFrom(from);
        criteria.setTo(to);
        rangeCriteria.add(criteria);
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PositionSelector getPositionSelector() {
        return positionSelector;
    }

    public void setPositionSelector(PositionSelector positionSelector) {
        this.positionSelector = positionSelector;
    }

}
