package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitParamsRunner.class)
public class CommonFilterTest {

    @Test
    @Parameters(method = "rangeCriteria")
    public void shouldReturnRangeCriteria(CommonFilter filter, RangeCriteria rangeCriteria){

        Iterator<RangeCriteria> iterator = filter.movementRangeCriteria().iterator();

        if (iterator.hasNext()) {
            assertEquals(rangeCriteria, iterator.next());

        }
    }

    @Test
    @Parameters(method = "listCriteria")
    public void shouldReturnListCriteria(CommonFilter filter, ListCriteria listCriteria){

        assertEquals(listCriteria, filter.movementListCriteria().iterator().next());

    }

    protected Object[] listCriteria(){

        CommonFilter filter = CommonFilter.builder()
                .positionSelector(PositionSelector.builder()
                        .selector(Selector.last)
                        .position(Position.positions).value(100.1F).build())
                .build();

        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.NR_OF_LATEST_REPORTS);
        criteria.setValue(String.valueOf(100));

        return $(
                $(filter, criteria)
        );
    }

    protected Object[] rangeCriteria(){

        final String from = "2013-02-28 12:24:56 +0100";
        String fromMinus24Hours = "2013-02-27 12:24:56 +0100";

        CommonFilter filter1 = new CommonFilter(){
            protected DateTime nowUTC() {
                return new DateTime(DateUtils.stringToDate(from));
            }
        };
        filter1.setPositionSelector(PositionSelector.builder()
                        .selector(Selector.last).value(24F)
                        .position(Position.hours).build()
        );
        RangeCriteria criteria1 = new RangeCriteria();
        criteria1.setKey(RangeKeyType.DATE);
        criteria1.setFrom(fromMinus24Hours);
        criteria1.setTo(from);

        String to = "2014-02-28 12:24:56 +0100";
        CommonFilter filter2 = CommonFilter.builder()
                .positionSelector(PositionSelector.builder()
                        .selector(Selector.all).build())
                .dateRange(new DateRange(DateUtils.stringToDate(from), DateUtils.stringToDate(to)))
                .build();

        RangeCriteria criteria2 = new RangeCriteria();
        criteria2.setKey(RangeKeyType.DATE);
        criteria2.setFrom(from);
        criteria2.setTo(to);


        CommonFilter filter3 = CommonFilter.builder()
                .positionSelector(PositionSelector.builder()
                        .selector(Selector.last).position(Position.positions).value(100F).build())
                .build();
        RangeCriteria empty = new RangeCriteria();
        empty.setKey(RangeKeyType.DATE);

        return $(
                $(filter1, criteria1),
                $(filter2, criteria2),
                $(filter3, empty)
        );
    }

    @Test
    public void shouldBeEqualWhenMerging() {

        CommonFilter filter;

        final Calendar calendar = new GregorianCalendar(2013, 1, 28, 13, 24, 56);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        Date endDate = calendar.getTime();

        CommonFilter incoming = CommonFilter.builder()
                .positionSelector(PositionSelector.builder().selector(Selector.all).build())
                .dateRange(new DateRange(startDate, endDate))
                .build();

        filter = CommonFilter.builder()
                .positionSelector(PositionSelector.builder()
                        .selector(Selector.last).value(1F).position(Position.hours).build())
                .build();

        assertNotEquals(filter, incoming);

        filter.merge(incoming);

        assertEquals(filter, incoming);

    }

}