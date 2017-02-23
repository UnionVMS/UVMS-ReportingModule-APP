/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.domain.DateRange;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.CommonFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.validation.CommonFilterIsValid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    public CommonFilter(DateRange dateRange, PositionSelector positionSelector) {
        super(common);
        this.dateRange = dateRange;
        this.positionSelector = positionSelector;
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
        if (Position.positions.equals(positionSelector.getPosition()) && positionSelector.getValue() != null) {
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

    @Override
    public List<SingleValueTypeFilter> getSingleValueFilters(DateTime now) {
        Selector selector = getPositionSelector().getSelector();
        Date startDate = null;
        Date endDate = null;
        List<SingleValueTypeFilter> faFilterTypes = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
        switch (selector) {
            case all:
                startDate = getDateRange().getStartDate();
                endDate = getDateRange().getEndDate();
                break;
            case last:
                if (getPositionSelector().getPosition().equals(Position.positions)) {
                    return Collections.emptyList();
                }
                Float hours = getPositionSelector().getValue();
                startDate = DateUtils.nowUTCMinusSeconds(now, hours).toDate();
                endDate = now.toDate();
                break;
        }
        if (startDate != null) {
            faFilterTypes.add(new SingleValueTypeFilter(SearchFilter.PERIOD_START, dateFormat.format(startDate)));
        }
        if (endDate != null) {
            faFilterTypes.add(new SingleValueTypeFilter(SearchFilter.PERIOD_END, dateFormat.format(endDate)));
        }
        return faFilterTypes;
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