package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.CommonFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.validation.CommonFilterIsValid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.common;

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
    public Object getUniqKey() {
        return getId();
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

    // UT
    protected DateTime nowUTC() {
        return DateUtils.nowUTC();
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
