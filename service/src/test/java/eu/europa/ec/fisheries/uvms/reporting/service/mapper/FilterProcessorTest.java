package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter.DateTimeFilterBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector.PositionSelectorBuilder;
import static junit.framework.TestCase.assertEquals;

public class FilterProcessorTest extends UnitilsJUnit4 {

    @TestedObject
    private FilterProcessor processor = new FilterProcessor();

    @Test
    @SneakyThrows
    public void testInitWithVesselFilter() {

        Set<Filter> filterList = new HashSet<>();

        VesselFilter vesselFilter = new VesselFilter();
        vesselFilter.setGuid("guid");
        filterList.add(vesselFilter);

        processor.init(filterList);

        assertEquals(1, processor.getVesselListCriteriaPairs().size());
        assertEquals(1, processor.getConnectIdMovements().size());
        assertEquals(0, processor.getVesselGroupList().size());
        assertEquals(0, processor.getMovementListCriteria().size());

        assertEquals(processor.getVesselListCriteriaPairs().get(0).getKey(), ConfigSearchField.GUID);
        assertEquals(processor.getVesselListCriteriaPairs().get(0).getValue(), "guid");

        assertEquals(processor.getConnectIdMovements().get(0).getKey(), SearchKey.CONNECT_ID);
        assertEquals(processor.getConnectIdMovements().get(0).getValue(), "guid");

    }

    @Test
    @SneakyThrows
    public void testInitWithVesselGroupFilter() {

        Set<Filter> filterList = new HashSet<>();

        VesselGroupFilter vesselGroupFilter = new VesselGroupFilter();
        vesselGroupFilter.setGroupId("1");
        vesselGroupFilter.setUserName("test");

        filterList.add(vesselGroupFilter);

        processor.init(filterList);

        assertEquals(0, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getConnectIdMovements().size());
        assertEquals(1, processor.getVesselGroupList().size());
        assertEquals(0, processor.getMovementListCriteria().size());

        assertEquals(processor.getVesselGroupList().get(0).getId(), new BigInteger("1"));
        assertEquals(processor.getVesselGroupList().get(0).getName(), null);
        assertEquals(processor.getVesselGroupList().get(0).getUser(), null);

    }

    @Test
    @SneakyThrows
    public void testInitWithDateTimeFilterALL() {

        Set<Filter> filterList = new HashSet<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 2);
        Date toDate = c.getTime();
        Date fromDate = new Date();

        CommonFilter dateTimeFilter = DateTimeFilterBuilder()
                .startDate(fromDate)
                .endDate(toDate)
                .positionSelector(PositionSelectorBuilder().selector(Selector.ALL).build())
                .build();

        filterList.add(dateTimeFilter);

        processor.init(filterList);

        assertEquals(0, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getConnectIdMovements().size());
        assertEquals(0, processor.getVesselGroupList().size());
        assertEquals(2, processor.getMovementListCriteria().size());

        assertEquals(processor.getMovementListCriteria().get(1).getKey(), SearchKey.TO_DATE);
        assertEquals(processor.getMovementListCriteria().get(1).getValue(), DateUtils.dateToString(toDate));
        assertEquals(processor.getMovementListCriteria().get(0).getKey(), SearchKey.FROM_DATE);
        assertEquals(processor.getMovementListCriteria().get(0).getValue(), DateUtils.dateToString(fromDate));
    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void testSanityFilterNull() {
        Set<Filter> filterList = new HashSet<>();
        processor.init(filterList);
    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void testSanityFilterSetNull() {
        processor.init(null);
    }

    @Test
    @SneakyThrows
    public void testInitWithDateTimeFilterLASTWithHours() {
        final DateTime dateNow = new DateTime();

        processor = new FilterProcessor(){
            protected DateTime nowUTC() {
                return dateNow;
            }
        };

        Set<Filter> filterList = new HashSet<>();
        int value = 2;

        CommonFilter dateTimeFilter = DateTimeFilterBuilder()
                .positionSelector(
                        PositionSelectorBuilder().selector(Selector.LAST).position(Position.HOURS).value(value).build()
                )
                .build();

        filterList.add(dateTimeFilter);

        processor.init(filterList);

        assertEquals(0, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getConnectIdMovements().size());
        assertEquals(0, processor.getVesselGroupList().size());
        assertEquals(2, processor.getMovementListCriteria().size());

        assertEquals(SearchKey.TO_DATE, processor.getMovementListCriteria().get(1).getKey());
        assertEquals(DateUtils.dateToString(dateNow.toDate()), processor.getMovementListCriteria().get(1).getValue());
        assertEquals(SearchKey.FROM_DATE, processor.getMovementListCriteria().get(0).getKey());
        assertEquals(DateUtils.dateToString(dateNow.minusHours(value).toDate()), processor.getMovementListCriteria().get(0).getValue());

    }

}
