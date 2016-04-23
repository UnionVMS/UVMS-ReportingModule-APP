package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.*;
import eu.europa.ec.fisheries.uvms.common.*;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.*;
import eu.europa.ec.fisheries.uvms.reporting.service.validation.*;
import lombok.*;
import org.joda.time.*;

import javax.persistence.*;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.*;

@Entity
@DiscriminatorValue("DATETIME")
@EqualsAndHashCode(callSuper = true)
@CommonFilterIsValid
@ToString
public class CommonFilter extends Filter {

    @Embedded
    private DateRange dateRange;

    @Embedded
    private PositionSelector positionSelector;

    public CommonFilter() {
        super(common);
    }

    @Builder
    public CommonFilter(Long id, DateRange dateRange, PositionSelector positionSelector) {
        super(common);
        this.dateRange = dateRange;
        this.positionSelector = positionSelector;
        setId(id);
        validate();
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitCommonFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        CommonFilterMapper.INSTANCE.merge((CommonFilter) filter, this);
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria(DateTime now) {
        List<RangeCriteria> rangeCriteria = new ArrayList<>();
        RangeCriteria date = CommonFilterMapper.INSTANCE.dateRangeToRangeCriteria(this);
        setDefaultValues(date, now);

        if (Position.hours.equals(positionSelector.getPosition())) {
            Float hours = positionSelector.getValue();
            Date to = DateUtils.nowUTCMinusSeconds(now, hours).toDate();
            rangeCriteria.add(CommonFilterMapper.INSTANCE.dateRangeToRangeCriteria(to, now.toDate()));
        } else {
            rangeCriteria.add(date);
        }

        return rangeCriteria;
    }

    private void setDefaultValues(final RangeCriteria date, DateTime now) {
        if (date.getTo() == null) {
            date.setTo(DateUtils.dateToString(now.toDate()));
        }
        if (date.getFrom() == null) {
            date.setFrom(DateUtils.dateToString(DateUtils.START_OF_TIME.toDate()));
        }
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        ListCriteria criteria = new ListCriteria();
        List<ListCriteria> listCriteria = new ArrayList<>();
        if (Position.positions.equals(positionSelector.getPosition())) {
            criteria = CommonFilterMapper.INSTANCE.positionToListCriteria(this);
        }
        if (criteria.getKey() != null) {
            listCriteria.add(criteria);
        }
        return listCriteria;
    }

    // UT
    protected DateTime nowUTC() {
        return DateUtils.nowUTC();
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public PositionSelector getPositionSelector() {
        return positionSelector;
    }

    public void setPositionSelector(PositionSelector positionSelector) {
        this.positionSelector = positionSelector;
    }

}
