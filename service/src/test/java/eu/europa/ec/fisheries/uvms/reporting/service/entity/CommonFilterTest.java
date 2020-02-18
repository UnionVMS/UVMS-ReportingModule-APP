/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.domain.DateRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitParamsRunner.class)
public class CommonFilterTest {

    public static final String START = "0001-01-03 00:00:00 +0000";
    final String NOW = "2013-02-28 12:24:56 +0000";
    final String FROM_MINUS_24_HOURS = "2013-02-27 12:24:56 +0000";
    final String TO = "2014-02-28 12:24:56 +0000";
    static TimeZone defaultTZ;

    @BeforeClass
    public static void init() {
        defaultTZ = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterClass
    public static void shutdown() {
        TimeZone.setDefault(defaultTZ);
    }


    @Test
    @Parameters(method = "rangeCriteria")
    public void shouldReturnRangeCriteria(CommonFilter filter, RangeCriteria rangeCriteria){

        Iterator<RangeCriteria> iterator = filter.movementRangeCriteria(new DateTime(DateUtils.stringToDate(NOW))).iterator();
        assertEquals(rangeCriteria, iterator.next());
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

        CommonFilter filter1 = new CommonFilter(){
            protected DateTime nowUTC() {
                return new DateTime(DateUtils.stringToDate(NOW));
            }
        };

        filter1.setPositionSelector(PositionSelector.builder()
                        .selector(Selector.last).value(24F)
                        .position(Position.hours).build()
        );
        RangeCriteria expectedCriteria = new RangeCriteria();
        expectedCriteria.setKey(RangeKeyType.DATE);
        expectedCriteria.setFrom(FROM_MINUS_24_HOURS);
        expectedCriteria.setTo(NOW);
        setDefaultValues(expectedCriteria);

        CommonFilter filter2 = CommonFilter.builder()
                .positionSelector(PositionSelector.builder()
                        .selector(Selector.all).build())
                .dateRange(new DateRange(DateUtils.stringToDate(NOW), DateUtils.stringToDate(TO)))
                .build();

        RangeCriteria expectedCriteria2 = new RangeCriteria();
        expectedCriteria2.setKey(RangeKeyType.DATE);
        expectedCriteria2.setFrom(NOW);
        expectedCriteria2.setTo(TO);
        setDefaultValues(expectedCriteria2);

        final Float hours = 100F;

        CommonFilter filter3 = new CommonFilter(){
            @Override
            protected DateTime nowUTC() {
                return new DateTime(DateUtils.stringToDate(NOW));
            }
        };

        filter3.setPositionSelector(PositionSelector.builder()
                .selector(Selector.last).position(Position.positions).value(hours).build());

        RangeCriteria expectedCriteria3 = new RangeCriteria();
        expectedCriteria3.setKey(RangeKeyType.DATE);
        expectedCriteria3.setFrom(START);
        expectedCriteria3.setTo(NOW);

        return $(
                $(filter1, expectedCriteria),
                $(filter2, expectedCriteria2),
                $(filter3, expectedCriteria3)
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

    private void setDefaultValues(final RangeCriteria date) {
        if (date.getTo() == null) {
            date.setFrom(DateUtils.dateToString(DateUtils.nowUTC().toDate())); // FIXME use offset
        }
        if (date.getFrom() == null) {
            date.setFrom(DateUtils.dateToString(new Date(Long.MIN_VALUE)));
        }
    }


}