package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CommonFilterTest extends UnitilsJUnit4 {

    @TestedObject
    private CommonFilter filter;

    @Before
    public void before(){
        filter = new CommonFilter();
    }

    @Test
    public void testMovementRangeCriteriaWithCommonFilterWithLastHours(){

        final Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);

        filter = new CommonFilter(){
            protected DateTime nowUTC() {
                return new DateTime(calendar.getTime());
            }

        };

        filter.setPositionSelector(PositionSelector.builder()
                        .selector(Selector.last)
                        .value(1F)
                        .position(Position.hours)
                        .build()
        );

        List<RangeCriteria> rangeCriteria = filter.movementRangeCriteria();

        assertEquals(1, rangeCriteria.size());
        assertEquals(DateUtils.stringToDate(rangeCriteria.get(0).getTo()), calendar.getTime());
        calendar.add(Calendar.HOUR, -1);
        assertEquals(DateUtils.stringToDate(rangeCriteria.get(0).getFrom()), calendar.getTime());
        assertEquals(rangeCriteria.get(0).getKey(), RangeKeyType.DATE);

    }

    @Test
    public void testMovementRangeCriteriaWithCommonFilterWithAll(){

        final Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        filter = new CommonFilter(){
            protected DateTime nowUTC() {
                return new DateTime(calendar.getTime());
            }

        };

        filter.setPositionSelector(PositionSelector.builder().selector(Selector.all).build());
        filter.setEndDate(endDate);
        filter.setStartDate(startDate);

        List<RangeCriteria> rangeCriteria = filter.movementRangeCriteria();

        assertEquals(1, rangeCriteria.size());
        assertEquals(rangeCriteria.get(0).getFrom(), DateUtils.parseUTCDateToString(startDate));
        assertEquals(rangeCriteria.get(0).getTo(), DateUtils.parseUTCDateToString(endDate));
        assertEquals(rangeCriteria.get(0).getKey(), RangeKeyType.DATE);

    }

    @Test
    public void testMerge(){

        final Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        Date endDate = calendar.getTime();

        CommonFilter incoming = CommonFilter.builder()
                .positionSelector(PositionSelector.builder().selector(Selector.all).build())
                .endDate(endDate)
                .startDate(startDate)
                .build();

        filter = CommonFilter.builder()
                .positionSelector(PositionSelector.builder().selector(Selector.last).value(1F).position(Position.hours).build())
                .endDate(new Date())
                .startDate(new Date())
                .build();

        assertNotEquals(filter, incoming);

        filter.merge(incoming);

        assertEquals(filter, incoming);

    }
}
